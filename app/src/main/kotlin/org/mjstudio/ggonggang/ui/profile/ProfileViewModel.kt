package org.mjstudio.ggonggang.ui.profile

import androidx.core.math.MathUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.enumerator.Major
import org.mjstudio.gfree.domain.enumerator.Sex
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository, private val userRepository: UserRepository, private val majorStrAdapter: Major.StringAdapter, private val sexStrAdapter: Sex.StringAdapter) : ViewModel() {

    private val TAG = ProfileViewModel::class.java.simpleName


    //region Datas
    var uid: String? = null

    val isLoading = MutableLiveData(true)

    val email: MutableLiveData<String> = MutableLiveData()
    val major: MutableLiveData<Major> = MutableLiveData()
    val majorText: LiveData<String> = Transformations.map(major) {
        majorStrAdapter.toUi(it)
    }
    val studentId: MutableLiveData<Int> = MutableLiveData()
    val studentIdText: LiveData<String> = Transformations.map(studentId) {
        it.toString()
    }
    val sex: MutableLiveData<Int> = MutableLiveData()
    val sexText: LiveData<String> = Transformations.map(sex) {
        sexStrAdapter.toUi(it)
    }
    val age: MutableLiveData<Int> = MutableLiveData()
    val ageText: LiveData<String> = Transformations.map(age) {
        val curYear = Constant.CURRENT_YEAR
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

    private fun initProfile() = viewModelScope.launch {
        isLoading.value = true
        try {
            authRepository.getUid()?.let { uid ->
                this@ProfileViewModel.uid = uid

                val user = userRepository.getUserInfo(uid)

                email.value = user.email
                studentId.value = user.studentId

                age.value = user.age
                sex.value = user.sex
                major.value = Major.getMajorWithCode(user.majorCode)
                initProfileComplete.value = true

            }
        } catch (e: Throwable) {
            debugE(TAG,e)
        } finally {
            isLoading.value = false
        }
    }

    private fun changeMajor(majorCode: String) = viewModelScope.launch {
        try {
            val major = userRepository.changeMajor(uid!!,majorCode)
            this@ProfileViewModel.major.value = Major.getMajorWithCode(major)
        }catch(e : Throwable) {
            msg.value = Once(NegativeMsg.PROFILE_CHANGE_FAIL)
        }
    }

    private fun changeId(id: Int) = viewModelScope.launch{
        try {
            val id = userRepository.changeStudentId(uid!!,id)
            this@ProfileViewModel.studentId.value = id
        }catch(e : Throwable) {
            msg.value = Once(NegativeMsg.PROFILE_CHANGE_FAIL)
        }
    }

    private fun changeAge(age: Int) = viewModelScope.launch{
        try {
            val age = userRepository.changeAge(uid!!,age)
            this@ProfileViewModel.age.value = age
        }catch(e : Throwable) {
            msg.value = Once(NegativeMsg.PROFILE_CHANGE_FAIL)
        }

    }

    private fun changeSex(sex: Int) = viewModelScope.launch{
        try {
            val sex = userRepository.changeSex(uid!!,sex)
            this@ProfileViewModel.sex.value = sex
        }catch(e : Throwable) {
            msg.value = Once(NegativeMsg.PROFILE_CHANGE_FAIL)
        }
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
        studentId.value?.let { id ->
            changeId(MathUtils.clamp(id.toInt() - 1, Constant.MIN_STUDENT_ID, Constant.MAX_STUDENT_ID))
        }
    }

    fun onClickIdRightButton() {
        studentId.value?.let { id ->
            changeId(MathUtils.clamp(id.toInt() + 1, Constant.MIN_STUDENT_ID, Constant.MAX_STUDENT_ID))
        }
    }

    fun onClickAgeLeftButton() {
        age.value?.let { age ->
            changeAge(MathUtils.clamp(age.toInt() + 1, Constant.MIN_AGE, Constant.MAX_AGE))

        }
    }

    fun onClickAgeRightButton() {
        age.value?.let { age ->
            changeAge(MathUtils.clamp(age.toInt() - 1, Constant.MIN_AGE, Constant.MAX_AGE))
        }
    }

    fun onClickSexLeftButton() {
        sex.value?.let { sex ->
            val curSex = Sex.getSexWithValue(sex.toInt())
            changeSex(Sex.getLeftSex(curSex).value)
        }
    }

    fun onClickSexRightButton() {
        sex.value?.let { sex ->
            val curSex = Sex.getSexWithValue(sex.toInt())
            changeSex(Sex.getRightSex(curSex).value)
        }
    }
}