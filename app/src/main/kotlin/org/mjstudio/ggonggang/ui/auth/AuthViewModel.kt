package org.mjstudio.ggonggang.ui.auth

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import org.mjstudio.gfree.domain.common.CustomMsg
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.NegativeMsg.EMAIL_NOT_EXIST
import org.mjstudio.gfree.domain.common.NegativeMsg.FIREBASE_ACCOUNT_CREATE_FAIL
import org.mjstudio.gfree.domain.common.NegativeMsg.SERVER_DISCONNECTED
import org.mjstudio.gfree.domain.common.NegativeMsg.SIGN_IN_FAIL
import org.mjstudio.gfree.domain.common.NegativeMsg.VERIFICATION_FAIL
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.PositiveMsg
import org.mjstudio.gfree.domain.common.PositiveMsg.SERVER_CONNECTED
import org.mjstudio.gfree.domain.common.PositiveMsg.SIGN_IN_COMPLETE
import org.mjstudio.gfree.domain.common.PositiveMsg.VERIFICATION_COMPLETE
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.dto.AccountDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.ServerRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.post
import org.mjstudio.ggonggang.ui.auth.AuthFragment.Companion.ACTION_PASSWORDRESET
import org.mjstudio.ggonggang.ui.auth.AuthFragment.Companion.ACTION_VERIFICATION
import org.mjstudio.ggonggang.ui.auth.AuthState.FIREBASE
import org.mjstudio.ggonggang.ui.auth.AuthState.SERVER
import org.mjstudio.ggonggang.ui.auth.AuthState.VERIFICATION
import retrofit2.HttpException
import java.util.concurrent.TimeUnit.*
import javax.inject.Inject

enum class AuthState(val state: String, val positive: PositiveMsg, val negative: NegativeMsg) {
    SERVER("Server", SERVER_CONNECTED, SERVER_DISCONNECTED), FIREBASE("Authentication", SIGN_IN_COMPLETE, SIGN_IN_FAIL), VERIFICATION("Verification", VERIFICATION_COMPLETE, VERIFICATION_FAIL)

    ;

    fun getNormalStateMessage(): String {
        return "$state - waiting..."
    }

    fun getPositiveStateMessage(): String {
        return "$state - ${positive.msg}"
    }

    fun getNegativeStateMessage(): String {
        return "$state - ${negative.msg}"
    }
}

typealias AccountValidationResult = Pair<Boolean, Msg>

class AuthViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository, private val userRepository: UserRepository, private val serverRepository: ServerRepository
) : ViewModel(), LifecycleObserver {

    private val TAG = AuthViewModel::class.java.simpleName
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    val msg: MutableLiveData<Once<Msg>> = MutableLiveData()
    val snackMsg: MutableLiveData<Once<Pair<Msg, SNACKBAR_ACTION?>>> = MutableLiveData()
    val navigateMainFragment: MutableLiveData<Once<UserInfoDTO>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    //region Datas
    val emailText: MutableLiveData<String> = MutableLiveData("")
    val passwordText: MutableLiveData<String> = MutableLiveData("")

    val signUpEmailText: MutableLiveData<String> = MutableLiveData("")
    val signUpPasswordText: MutableLiveData<String> = MutableLiveData("")
    val signUpPasswordCheckText: MutableLiveData<String> = MutableLiveData("")

    val textServerConnection = MutableLiveData(
            AuthState.SERVER.getNormalStateMessage()
    )
    val textServerConnectionColor = MutableLiveData(R.color.textInActivate)
    val textAuthentication = MutableLiveData(
            AuthState.FIREBASE.getNormalStateMessage()
    )
    val textAuthenticationColor = MutableLiveData(R.color.textInActivate)
    val textVerification = MutableLiveData(
            AuthState.VERIFICATION.getNormalStateMessage()
    )
    val textVerificationColor = MutableLiveData(R.color.textInActivate)

//    val text
    //endregion

    //region Event
    val showSignUpDialog: MutableLiveData<Once<AccountDTO>> = MutableLiveData()
    val signUpCancel: MutableLiveData<Once<Boolean>> = MutableLiveData()
    val signUpSubmit: MutableLiveData<Once<Boolean>> = MutableLiveData()
    //endregion

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    /**
     * 이미 FirebaseAuth에 로그인이 되어있고, 서버에 유저 정보가 있는 지 확인하는 메서드
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun checkAlreadyFirebaseLogin() {
        isLoading post true
        val user = authRepository.getCurrentUser()

        // 로그인이 안되어있는 상태
        if (user == null) {
            isLoading post false
        }
        // 유저가 로그인 되어있는 상태
        else {
            firebaseSignInComplete(user)
        }


    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun checkAPIServerIsConnected() {
        compositeDisposable += serverRepository.isServerRunning()
                .timeout(2, SECONDS)
                .addSchedulers()
                .subscribe({
                    changeAuthState(SERVER, true)
                }, {
                    changeAuthState(SERVER, false, it)
                })
    }

    private fun changeAuthState(state: AuthState, success: Boolean, ex: Throwable? = null) {
        when (state) {
            SERVER -> {
                if (success) {
                    textServerConnection post AuthState.SERVER.getPositiveStateMessage()
                    textServerConnectionColor post R.color.authBlue
                } else {
                    textServerConnection post AuthState.SERVER.getNegativeStateMessage()
                    textServerConnectionColor post R.color.authRed
                }
            }
            FIREBASE -> {
                if (success) {
                    textAuthentication post AuthState.FIREBASE.getPositiveStateMessage()
                    textAuthenticationColor post R.color.authBlue
                } else {
                    textAuthentication post AuthState.FIREBASE.getNegativeStateMessage()
                    textAuthenticationColor post R.color.authRed

                    changeAuthState(AuthState.VERIFICATION,false)
                }
            }
            VERIFICATION -> {
                if (success) {
                    textVerification post AuthState.VERIFICATION.getPositiveStateMessage()
                    textVerificationColor post R.color.authBlue
                } else {
                    textVerification post AuthState.VERIFICATION.getNegativeStateMessage()
                    textVerificationColor post R.color.authRed
                }
            }
        }
    }

    /**
     * Firebase에 SignIn이 되었을 경우
     */
    private fun firebaseSignInComplete(firebaseUser: FirebaseUser) {
        emailText post (firebaseUser.email ?: "")
        changeAuthState(FIREBASE, true)

        // 만약 이메일이 인증되었을 경우, 서버에 유저 정보가 있는 지 확인
        if (firebaseUser.isEmailVerified) {
            checkOrCreateUserInServer(firebaseUser)
            changeAuthState(VERIFICATION, true)
        }
        // 이메일이 아직 인증된 상태가 아니라면 인증을 보내고 재 인증 스낵바 메시지를 보냄
        else {
            changeAuthState(VERIFICATION, false)
            isLoading post false
            snackMsg post Once(NegativeMsg.EMAIL_NEED_VERIFICATION to ACTION_VERIFICATION)
        }
    }

    /**
     * 로그인을 시도하는 메서드
     *
     * @param account A Instance of [AccountDTO] for check Firebase Auth Sign-In process
     */
    private fun signIn(account: AccountDTO) {
        isLoading post true
        val validationResult = signInValidation(account)
        if (!validationResult.first) {
            changeAuthState(FIREBASE, false)
            isLoading post false
            snackMsg post Once(validationResult.second to null)
        } else {
            compositeDisposable += authRepository.signIn(account).addSchedulers().subscribe({ result ->
                        // SIGN IN SUCCESS
                        firebaseSignInComplete(result.user)
                    }, { throwable ->
                        // SIGN IN FAIL
                        signInFail(throwable)
                    })
        }
    }

    /**
     * SignIn 과정에서 요청한 아이디,패스워드 Input Values가 Valid한 지 검사해주는 메서드
     */
    private fun signInValidation(account: AccountDTO): AccountValidationResult {
        if (account.email.isEmpty()) {
            return false to NegativeMsg.EMAIL_EMPTY
        } else if (!account.email.contains("@gist.ac.kr")) {
            return false to NegativeMsg.EMAIL_GIST_FORMAT
        } else if (account.password.length < 6) {
            return false to NegativeMsg.PASSWORD_LENGTH6
        }

        return true to PositiveMsg.SUCCESS
    }
    /**
     * 로그인에 실패
     */
    private fun signInFail(exception: Throwable) {
        changeAuthState(FIREBASE, false)
        isLoading post false
        if (exception is FirebaseAuthInvalidCredentialsException) { // 비밀번호가 잘못되었을 때
            snackMsg post Once(NegativeMsg.PASSWORD_INCORRECT to ACTION_PASSWORDRESET)
        } else if (exception is FirebaseAuthInvalidUserException) { // 계정이 존재하지 않을 때
            snackMsg post Once(EMAIL_NOT_EXIST to null)
        } else {
            snackMsg post Once(CustomMsg(exception.message ?: "") to null)
        }
    }

    /**
     * 서버에 유저가 있는지 확인하는 메서드
     * 서버에 유저가 있다면, 정상적으로 로그인을 진행
     * 서버에 유저가 없다면, 네트워크 문제인지 다른 문제인지 확인후 분기
     *
     * @param firebaseUser uid를 이용해서 서버에 유저 정보가 존재하는 지 확인하는 데 쓰이는 인자
     */
    private fun checkOrCreateUserInServer(firebaseUser: FirebaseUser) {
            val uid = firebaseUser.uid
            val email = firebaseUser.email

        compositeDisposable += userRepository.getUserInfo(uid)
                    .addSchedulers()
                    .doAfterTerminate {
                        isLoading post false
                    }.subscribe({ user ->
                    // user is exist in server
                        navigateMainFragment post Once(user)
                    }, {
                    isLoading post false
                        debugE("무슨 에러가 날까요 ${it}${it.message}")
                    // 1. network connection is not good
                    // 2. user are not exist in server or else

                    when (it) {
                            is java.net.ConnectException -> {
                                snackMsg post Once(NegativeMsg.SERVER_ACCOUNT_CREATE_FAIL to null)
                            }
                        is HttpException -> {
                            // 아이디가 없다

                            userRepository.createAccount(UserInfoDTO(uid, email ?: "", Constant.DEFAULT_MAJOR,
                                    Constant.DEFAULT_STUDENT_ID, Constant.DEFAULT_SEX, Constant.DEFAULT_AGE))
                                        .addSchedulers()
                                        .doAfterTerminate {
                                            isLoading post false
                                        }
                                        .subscribe({
                                            // 서버에 아이디 생성 성공
                                            navigateMainFragment post Once(it)
                                        }, {
                                            // 에러
                                            debugE(it)
                                            snackMsg post Once(NegativeMsg
                                                    .SERVER_ACCOUNT_CREATE_FAIL to null)
                                        })
                            }
                        else -> {
                                msg post Once(NegativeMsg.FAIL)
                            }
                        }
                    })
    }

    /**
     * 파이어베이스 계정을 만드는 메서드
     */
    private fun signUpFirebaseAccount(account: AccountDTO, passwordCheck: String) {
        isLoading post true
        val validationResult = accountValidation(account, passwordCheck)

        // 만약 유효하지 않을 경우
        if (!validationResult.first) {
            msg post Once(validationResult.second)
            isLoading post false
            return
        }
        // 가입이 가능한 경우
        else {
            compositeDisposable += authRepository
                    .createFirebaseAccount(account)
                    .addSchedulers()
                    .subscribe({
                        signUpServerAccount(it.user)
                    }, {
                        isLoading post false
                        msg post Once(FIREBASE_ACCOUNT_CREATE_FAIL)
                    })
        }
    }
    /**
     * 서버에 계정을 만드는 메서드 [signUpFirebaseAccount] 가 성공할 시에 호출된다.
     *
     * @see signUpFirebaseAccount
     */
    private fun signUpServerAccount(user: FirebaseUser) {
        compositeDisposable += userRepository.createAccount(UserInfoDTO(user.uid, user.email
                ?: "", Constant.DEFAULT_MAJOR, Constant.DEFAULT_STUDENT_ID, Constant.DEFAULT_SEX, Constant.DEFAULT_AGE)
        ).addSchedulers()
                .doAfterTerminate {
                    isLoading post false
                }
                .subscribe({
                    // 서버에 계정 만들기 성공
                    snackMsg post Once(PositiveMsg.ACCOUNT_CREATED to null)
                }, {
                    // 서버에 계정 만들기 실패
                    msg post Once(NegativeMsg.SERVER_ACCOUNT_CREATE_FAIL)
                })
    }

    /**
     * 해당 계정 정보와 패스워드 2차 검사를 통해 가입이 가능한 형식인지 검사한다.
     *
     * @return 가입이 가능한 여부, 에러 메시지
     */

    private fun accountValidation(account: AccountDTO, passwordCheck: String): AccountValidationResult {
        if (account.password != passwordCheck) {
            return false to NegativeMsg.PASSWORD_NOT_EQUAL
        } else if (account.email.isEmpty()) {
            return false to NegativeMsg.EMAIL_EMPTY
        } else if (!account.email.contains("@gist.ac.kr")) {
            return false to NegativeMsg.EMAIL_GIST_FORMAT
        } else if (account.password.length < 6) {
            return false to NegativeMsg.PASSWORD_LENGTH6
        }
        return true to PositiveMsg.SUCCESS
    }

    /**
     * Send VerificationEmail to Firebase
     */
    fun sendVerificationEmail() {
        isLoading post true
        authRepository.getCurrentUser()?.let {
            compositeDisposable += authRepository.sendEmailVerification(it)
                    .addSchedulers()
                    .doOnTerminate {
                        isLoading post false
                    }
                    .subscribe({
                        msg post Once(PositiveMsg.SEND_VERIFICATION_EMAIL)
                    }, {
                        msg post Once(NegativeMsg.FAIL_SEND_VERIFICATION_EMAIL)
                    })
        }
    }

    /**
     * Send Password reset request email to Firebase
     */
    fun sendPasswordResetEmail() {

        val user = authRepository.getCurrentUser() ?: return
        val email = user.email ?: return
        isLoading post true

        compositeDisposable += authRepository.sendPasswordResetEmail(email)
                .addSchedulers()
                .doOnTerminate {
                    isLoading post false
                }
                .subscribe({
                    msg post Once(PositiveMsg.SEND_PASSWORD_REST_EMAIL)
                }, {
                    msg post Once(NegativeMsg.FAIL_SEND_RESET_PASSWORD_EMAIL)
                })
    }

    fun onClickSignInButton() {
        signIn(AccountDTO(emailText.value ?: "", passwordText.value ?: ""))
    }
    fun onClickSignUpButton() {
        showSignUpDialog post Once(AccountDTO(emailText.value ?: "", passwordText.value ?: ""))
    }
    fun onClickSignUpCancel() {
        signUpCancel post Once(true)
    }
    fun onClickSignUpSubmit() {
        signUpSubmit post Once(true)
        signUpFirebaseAccount(AccountDTO(signUpEmailText.value ?: "", signUpPasswordText.value ?: ""),
                signUpPasswordCheckText.value ?: ""
        )
    }
}