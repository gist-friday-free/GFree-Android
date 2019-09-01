package org.mjstudio.ggonggang.ui.auth

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
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
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.dto.AccountDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.ServerRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.ui.auth.AuthFragment.Companion.ACTION_PASSWORDRESET
import org.mjstudio.ggonggang.ui.auth.AuthFragment.Companion.ACTION_VERIFICATION
import org.mjstudio.ggonggang.ui.auth.AuthState.FIREBASE
import org.mjstudio.ggonggang.ui.auth.AuthState.SERVER
import org.mjstudio.ggonggang.ui.auth.AuthState.VERIFICATION
import retrofit2.HttpException
import java.util.concurrent.*
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

class AuthViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository, private val userRepository: UserRepository, private val serverRepository: ServerRepository) : ViewModel(), LifecycleObserver {

    private val TAG = AuthViewModel::class.java.simpleName
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    val msg: MutableLiveData<Once<Msg>> = MutableLiveData()
    val snackMsg: MutableLiveData<Once<Pair<Msg, SNACKBAR_ACTION?>>> = MutableLiveData()
    val navigateMainFragment: MutableLiveData<Once<UserInfoDTO>> = MutableLiveData()

    //region Datas
    val emailText: MutableLiveData<String> = MutableLiveData("")
    val passwordText: MutableLiveData<String> = MutableLiveData("")

    val signUpEmailText: MutableLiveData<String> = MutableLiveData("")
    val signUpPasswordText: MutableLiveData<String> = MutableLiveData("")
    val signUpPasswordCheckText: MutableLiveData<String> = MutableLiveData("")

    val textServerConnection = MutableLiveData(AuthState.SERVER.getNormalStateMessage())
    val textServerConnectionColor = MutableLiveData(R.color.textInActivate)
    val textAuthentication = MutableLiveData(AuthState.FIREBASE.getNormalStateMessage())
    val textAuthenticationColor = MutableLiveData(R.color.textInActivate)
    val textVerification = MutableLiveData(AuthState.VERIFICATION.getNormalStateMessage())
    val textVerificationColor = MutableLiveData(R.color.textInActivate)

    //    val text
    //endregion

    //region Event
    val showSignUpDialog: MutableLiveData<Once<AccountDTO>> = MutableLiveData()
    val signUpCancel: MutableLiveData<Once<Boolean>> = MutableLiveData()
    val signUpSubmit: MutableLiveData<Once<Boolean>> = MutableLiveData()
    //endregion


    /**
     * 이미 FirebaseAuth에 로그인이 되어있고, 서버에 유저 정보가 있는 지 확인하는 메서드
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun checkAlreadyFirebaseLogin() {

        viewModelScope.launch {
            isLoading.value = true
            val user = authRepository.getCurrentUser()

            // 로그인이 안되어있는 상태
            if (user == null) {
                isLoading.value = false
            }
            // 유저가 로그인 되어있는 상태
            else {
                firebaseSignInComplete(user)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun checkAPIServerIsConnected() = viewModelScope.launch {
        try {
            val result = withTimeoutOrNull(2000) {
                if (serverRepository.isServerRunning()) {
                    changeAuthState(SERVER, true)
                } else {
                    changeAuthState(SERVER, false)
                }
            }
            result ?: changeAuthState(SERVER, false, TimeoutException("time out"))
        } catch (t: Throwable) {
            debugE(TAG, t)
            changeAuthState(SERVER, false)
        }


    }

    private fun changeAuthState(state: AuthState, success: Boolean, ex: Throwable? = null) {
        when (state) {
            SERVER -> {
                if (success) {
                    textServerConnection.value = AuthState.SERVER.getPositiveStateMessage()
                    textServerConnectionColor.value = R.color.authBlue
                } else {
                    textServerConnection.value = AuthState.SERVER.getNegativeStateMessage()
                    textServerConnectionColor.value = R.color.authRed
                }
            }
            FIREBASE -> {
                if (success) {
                    textAuthentication.value = AuthState.FIREBASE.getPositiveStateMessage()
                    textAuthenticationColor.value = R.color.authBlue
                } else {
                    textAuthentication.value = AuthState.FIREBASE.getNegativeStateMessage()
                    textAuthenticationColor.value = R.color.authRed

                    changeAuthState(AuthState.VERIFICATION, false)
                }
            }
            VERIFICATION -> {
                if (success) {
                    textVerification.value = AuthState.VERIFICATION.getPositiveStateMessage()
                    textVerificationColor.value = R.color.authBlue
                } else {
                    textVerification.value = AuthState.VERIFICATION.getNegativeStateMessage()
                    textVerificationColor.value = R.color.authRed
                }
            }
        }
    }

    /**
     * Firebase에 SignIn이 되었을 경우
     */
    private fun firebaseSignInComplete(firebaseUser: FirebaseUser) {
        emailText.value = (firebaseUser.email ?: "")
        changeAuthState(FIREBASE, true)

        // 만약 이메일이 인증되었을 경우, 서버에 유저 정보가 있는 지 확인
        if (firebaseUser.isEmailVerified) {
            checkOrCreateUserInServer(firebaseUser)
            changeAuthState(VERIFICATION, true)
        }
        // 이메일이 아직 인증된 상태가 아니라면 인증을 보내고 재 인증 스낵바 메시지를 보냄
        else {
            changeAuthState(VERIFICATION, false)
            isLoading.value = false
            snackMsg.value = Once(NegativeMsg.EMAIL_NEED_VERIFICATION to ACTION_VERIFICATION)
        }
    }

    /**
     * 로그인을 시도하는 메서드
     *
     * @param account A Instance of [AccountDTO] for check Firebase Auth Sign-In process
     */
    private fun signIn(account: AccountDTO) = viewModelScope.launch {
        try {
            isLoading.value = true

            val validationResult = signInValidation(account)

            if (!validationResult.first) {
                changeAuthState(FIREBASE, false)
                isLoading.value = false
                snackMsg.value = Once(validationResult.second to null)
            } else {
                val user = authRepository.signIn(account).user

                if (user == null) {
                    signInFail(NullPointerException("user is null"))
                } else {
                    firebaseSignInComplete(user)
                }

            }
        }catch(t : Throwable) {
            debugE(TAG,t)
            signInFail(t)
        }finally {
            isLoading.value = false
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
        isLoading.value = false
        when(exception) {
            is FirebaseAuthInvalidCredentialsException-> {
                // 비밀번호가 잘못되었을 때
                snackMsg.value = Once(NegativeMsg.PASSWORD_INCORRECT to ACTION_PASSWORDRESET)
            }

            is FirebaseAuthInvalidUserException-> {
                // 계정이 존재하지 않을 때
                snackMsg.value = Once(EMAIL_NOT_EXIST to null)
            }
            else-> {
                snackMsg.value = Once(CustomMsg(exception.message ?: "") to null)
            }
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

        viewModelScope.launch {

            isLoading.value = true

            try {
                coroutineScope {
                    val user = userRepository.getUserInfo(uid)
                    navigateMainFragment.value = Once(user)
                }
            } catch (e: Exception) {
                debugE("무슨 에러가 날까요 $e")
                // 1. network connection is not good
                // 2. user are not exist in server or else

                when (e) {
                    is java.net.ConnectException -> {
                        snackMsg.value = Once(NegativeMsg.SERVER_ACCOUNT_CREATE_FAIL to null)
                    }
                    is HttpException -> {
                        // 아이디가 없다

                        signUpServerAccount(firebaseUser)
                    }
                    else -> {
                        msg.value = Once(NegativeMsg.FAIL)
                    }
                }
            } finally {
                isLoading.value = false
            }


        }


    }



    /**
     * 파이어베이스 계정을 만드는 메서드
     */
    private fun signUpFirebaseAccount(account: AccountDTO, passwordCheck: String) {
        isLoading.value = true
        val validationResult = accountValidation(account, passwordCheck)

        // 만약 유효하지 않을 경우
        if (!validationResult.first) {
            msg.value = Once(validationResult.second)
            isLoading.value = false
            return
        }
        // 가입이 가능한 경우
        else {

            viewModelScope.launch {
                val result = authRepository.createFirebaseAccount(account)
                if (result.user == null) {
                    isLoading.value = false
                    msg.value = Once(FIREBASE_ACCOUNT_CREATE_FAIL)
                } else {
                    signUpServerAccount(result.user!!)
                }

            }
        }
    }


    /**
     * 서버에 계정을 만드는 메서드 [signUpFirebaseAccount] 가 성공할 시에 호출된다.
     *
     * @see signUpFirebaseAccount
     */
    private suspend fun signUpServerAccount(user: FirebaseUser) = try {
        isLoading.value = true
        coroutineScope {

            userRepository.createAccount(UserInfoDTO(user.uid, user.email
                    ?: "", Constant.DEFAULT_MAJOR, Constant.DEFAULT_STUDENT_ID, Constant.DEFAULT_SEX, Constant.DEFAULT_AGE))
        }

        // 서버에 계정 만들기 성공
        snackMsg.value = Once(PositiveMsg.ACCOUNT_CREATED to null)
        isLoading.value = false
    } catch (e: Exception) {
        // 서버에 계정 만들기 실패
        msg.value = Once(NegativeMsg.SERVER_ACCOUNT_CREATE_FAIL)
        isLoading.value = false
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

    fun onSendVerificationEmail() {
        viewModelScope.launch { sendVerificationEmail() }
    }

    fun onSendPasswordResetEmail() {
        viewModelScope.launch { sendPasswordResetEmail() }
    }

    /**
     * Send VerificationEmail to Firebase
     */
    private suspend fun sendVerificationEmail() = try {
        coroutineScope {
            isLoading.value = true


        }


        authRepository.getCurrentUser()?.let {
            authRepository.sendEmailVerification(it)
            msg.value = Once(PositiveMsg.SEND_VERIFICATION_EMAIL)
            isLoading.value = false
            return@let
        }
    } catch (e: Exception) {
        msg.value = Once(NegativeMsg.FAIL_SEND_VERIFICATION_EMAIL)
        isLoading.value = false
    }

    /**
     * Send Password reset request email to Firebase
     */
    private suspend fun sendPasswordResetEmail() = try {
        coroutineScope {
            val user = authRepository.getCurrentUser()
            val email = user?.email

            if (user == null || email == null) throw java.lang.NullPointerException("User or email is null")

            isLoading.value = true
            authRepository.sendPasswordResetEmail(email)
        }
        msg.value = Once(PositiveMsg.SEND_PASSWORD_REST_EMAIL)
        isLoading.value = false
    } catch (e: Exception) {
        msg.value = Once(NegativeMsg.FAIL_SEND_RESET_PASSWORD_EMAIL)
        isLoading.value = false
    }

    fun onClickSignInButton() {
        signIn(AccountDTO(emailText.value ?: "", passwordText.value ?: ""))
    }

    fun onClickSignUpButton() {
        showSignUpDialog.value = Once(AccountDTO(emailText.value ?: "", passwordText.value ?: ""))
    }

    fun onClickSignUpCancel() {
        signUpCancel.value = Once(true)
    }

    fun onClickSignUpSubmit() {
        signUpSubmit.value = Once(true)
        signUpFirebaseAccount(AccountDTO(signUpEmailText.value ?: "", signUpPasswordText.value
                ?: ""), signUpPasswordCheckText.value ?: "")
    }
}