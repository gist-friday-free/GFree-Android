package org.mjstudio.ggonggang.ui.profile

import androidx.core.math.MathUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.enumerator.Major
import org.mjstudio.gfree.domain.enumerator.Sex
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository,
        private val userRepository: UserRepository,
        private val majorStrAdapter : Major.StringAdapter,
        private val sexStrAdapter : Sex.StringAdapter
) : ViewModel(){

    private val TAG = ProfileViewModel::class.java.simpleName

    private val compositeDisposable = CompositeDisposable()


    //region Datas
    var uid: String? = null

    val isLoading = MutableLiveData(true)

    val email : MutableLiveData<String> = MutableLiveData()
    val major : MutableLiveData<Major> = MutableLiveData()
    val majorText:LiveData<String> = Transformations.map(major) {
        majorStrAdapter.toUi(it)
    }
    val studentId : MutableLiveData<Int> = MutableLiveData()
    val studentIdText:LiveData<String> = Transformations.map(studentId) {
        it.toString()
    }
    val sex : MutableLiveData<Int> = MutableLiveData()
    val sexText :LiveData<String> = Transformations.map(sex) {
        sexStrAdapter.toUi(it)
    }
    val age : MutableLiveData<Int> = MutableLiveData()
    val ageText:LiveData<String> = Transformations.map(age) {
        val curYear= Constant.CURRENT_YEAR
        (curYear - it + 1).toString()
    }

    val gradeCountText: LiveData<String> = Transformations.map(userRepository.getRegisteredClassDataLiveData()) {
        it.sumBy { it.grade ?: 0 }.toString() + " 학점"
    }


    val initProfileComplete: MutableLiveData<Boolean> = MutableLiveData(false)
    //endregion

    //region Event
    val msg = MutableLiveData<Once<Msg>>()
    val snackMsg = MutableLiveData<Once<Msg>>()

    //endregion

    val adapter = MutableLiveData(ProfileAdapter())

    init {
        initProfile()
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    private fun initProfile() {
        authRepository.getUid()?.let { uid ->
            this@ProfileViewModel.uid = uid
            userRepository.getUserInfo(uid)
                    .addSchedulers()
                    .doAfterTerminate {
                        isLoading.value = false
                    }
                    .subscribe({ user ->
                        email.value = user.email
                        studentId.value = user.studentId
                        major.value = Major.getMajorWithCode(user.majorCode)
                        age.value = user.age
                        sex.value = user.sex

                        initProfileComplete.value = true
                    }, {
                        msg.value = Once(NegativeMsg.PROFILE_INIT_FAIL)
                    })
        }
    }

    private fun changeMajor(majorCode: String) {
        compositeDisposable+=userRepository.changeMajor(uid!!, majorCode)
                .addSchedulers()
                .subscribe({newMajor->
                    major.value = Major.getMajorWithCode(newMajor)
                }, {
                    msg.value = Once(NegativeMsg.PROFILE_CHANGE_FAIL)
                })

    }

    private fun changeId(id: Int) {
        compositeDisposable += userRepository.changeStudentId(uid!!, id)
                .addSchedulers()
                .subscribe({newId->
                    studentId.value = newId
                }, {
                    msg.value = Once(NegativeMsg.PROFILE_CHANGE_FAIL)
                })
    }

    private fun changeAge(age: Int) {

        compositeDisposable+=userRepository.changeAge(uid!!, age)
                .addSchedulers()
                .subscribe({newAge->
                    this@ProfileViewModel.age.value = newAge
                }, {
                    msg.value = Once(NegativeMsg.PROFILE_CHANGE_FAIL)
                })
    }

    private fun changeSex(sex: Int) {
        compositeDisposable+=userRepository.changeSex(uid!!, sex)
                .addSchedulers()
                .subscribe({newSex->
                    this@ProfileViewModel.sex.value = newSex
                }, {
                    msg.value = Once(NegativeMsg.PROFILE_CHANGE_FAIL)
                })
    }

    fun onClickMajorLeftButton() {
        major.value?.let { major ->
            val curMajor = major

            changeMajor(Major.getLeftMajor(curMajor).code)
        }

    }
    fun onClickMajorRightButton() {
        major.value?.let { major ->
            val curMajor = major

            changeMajor(Major.getRightMajor(curMajor).code)
        }
    }
    fun onClickIdLeftButton() {
        studentId.value?.let {id->
            changeId(MathUtils.clamp(id.toInt()-1, Constant.MIN_STUDENT_ID,Constant.MAX_STUDENT_ID))
        }
    }
    fun onClickIdRightButton() {
        studentId.value?.let {id->
            changeId(MathUtils.clamp(id.toInt()+1, Constant.MIN_STUDENT_ID,Constant.MAX_STUDENT_ID))
        }
    }
    fun onClickAgeLeftButton() {
        age.value?.let {age->
            changeAge(MathUtils.clamp(age.toInt()+1,Constant.MIN_AGE,Constant.MAX_AGE))

        }
    }
    fun onClickAgeRightButton() {
        age.value?.let {age->
            changeAge(MathUtils.clamp(age.toInt()-1,Constant.MIN_AGE,Constant.MAX_AGE))
        }
    }
    fun onClickSexLeftButton() {
        sex.value?.let {sex->
            val curSex = Sex.getSexWithValue(sex.toInt())
            changeSex(Sex.getLeftSex(curSex).value)
        }
    }
    fun onClickSexRightButton() {
        sex.value?.let {sex->
            val curSex = Sex.getSexWithValue(sex.toInt())
            changeSex(Sex.getRightSex(curSex).value)
        }
    }
}