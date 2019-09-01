package org.mjstudio.ggonggang.ui.post

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mjstudio.gfree.domain.adapter.toDTO
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.EditDTO.Type.ADD
import org.mjstudio.gfree.domain.dto.EditDTO.Type.CODE
import org.mjstudio.gfree.domain.dto.EditDTO.Type.GRADE
import org.mjstudio.gfree.domain.dto.EditDTO.Type.NAME
import org.mjstudio.gfree.domain.dto.EditDTO.Type.PLACE
import org.mjstudio.gfree.domain.dto.EditDTO.Type.PROFESSOR
import org.mjstudio.gfree.domain.dto.EditDTO.Type.REMOVE
import org.mjstudio.gfree.domain.dto.EditDTO.Type.SIZE
import org.mjstudio.gfree.domain.dto.EditDTO.Type.TIMEADD
import org.mjstudio.gfree.domain.dto.EditDTO.Type.TIMEREMOVE
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.entity.Edit
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import org.mjstudio.gfree.domain.repository.EditRepository
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import javax.inject.Inject

class PostViewModel @Inject constructor(private val app: Application, private val classDataRepository: ClassDataRepository, private val editRepository: EditRepository, private val userRepository: UserRepository, private val authRepository: FirebaseAuthRepository) : ViewModel() {

    private val TAG = PostViewModel::class.java.simpleName

    //region DATA

    /**
     * 현재 EditText에 표시되는 글씨
     */
    val codeText = MutableLiveData("")

    /**
     * EditText를 관찰하며 현재 선택된 ClassData를 변경해주는 옵저버
     */


    val currentClassDataObserver = Transformations.switchMap(codeText) { code ->

        liveData(viewModelScope.coroutineContext) {

            try {
                val it = classDataRepository.getClassDataWithCode(code.toUpperCase(), Constant.CURRENT_YEAR, Constant.CURRENT_SEMESTER)
                emit(it)
            } catch (e: Throwable) {
                emit(null)
                debugE(TAG, "cannot find class With Code")
            }

        }
    }
    /**
     * 현재 선택된 ClassData
     */
    val currentClassData: MutableLiveData<ClassData?> = MutableLiveData(null)

    /**
     * 현재의 EditType
     */
    val selectedType = MutableLiveData<EditDTO.Type?>(null)

    /**
     * EditType 으로 표시되는 텍스트
     */
    val typeText = Transformations.map(selectedType) {
        if (it == null) return@map ""

        app.resources.getString(it.strResId)
    }

    /**
     * 현재 과목의 EditType에 해당하는 값 텍스트, Inverse Binding
     */
    val currentValueText = MediatorLiveData<String>().apply {
        this.addSource(currentClassData) { classData ->
            classData ?: return@addSource

            synchronized(currentClassData) {
                if (selectedType.value != null) {
                    setViewConstraintsWithCurrentClassDataAndEditType(classData, selectedType.value!!, true)
                }
            }
        }
        this.addSource(selectedType) { type ->
            type ?: return@addSource

            synchronized(currentClassData) {
                if (currentClassData.value != null) {
                    setViewConstraintsWithCurrentClassDataAndEditType(currentClassData.value!!, type, false)
                }
            }
        }

        this.value = ""
    }

    /**
     * RecyclerView에서 보여줄 TimeSlot 아이템들
     */
    val timeSlotItems = Transformations.map(currentClassData) { classData ->
        classData?.time ?: listOf()
    }

    /**
     * NewValueText에 해당하는 값 텍스트, Inverse Binding
     */
    val newValueText = MutableLiveData("")

    /**
     * CurrentValue EditText와 NewValue EditText의 Visibility
     */
    val editTextVisibility = MutableLiveData<Boolean>(false)


    /**
     * ClassInfo에 해당하는 텍스트 "Cannot find" or 과목 이름
     */
    val classInfoText: LiveData<String> = Transformations.map(currentClassData) { classData ->
        if (classData == null) {
            return@map "Cannot find"
        } else {
            return@map classData.name
        }
    }

    //endregion


    //region EVENT
    val msg = MutableLiveData<Once<Msg>>()
    val snackMsg = MutableLiveData<Once<Msg>>()

    val clickTypeSelect = MutableLiveData<Once<Boolean>>()
    val clickNavigateInfo = MutableLiveData<Once<ClassData>>()
    val clickBackButton = MutableLiveData<Once<Boolean>>()
    val removeRequest = MutableLiveData<Once<ClassData>>()
    val requestSuccess = MutableLiveData<Once<EditDTO>>()

    val duplicateItemExist = MutableLiveData<Once<EditDTO>>()

    //endregion

    //region STATE
    val loading = MutableLiveData<Boolean>(false)
    //endregion

    private fun setViewConstraintsWithCurrentClassDataAndEditType(classData: ClassData, type: EditDTO.Type, fromClassDataChange: Boolean) {

        //EditText Visibility settings
        editTextVisibility.value = when (type) {
            ADD, REMOVE, TIMEADD, TIMEREMOVE -> false
            CODE, NAME, GRADE, PROFESSOR, PLACE, SIZE -> true
        }

        //EditText Text settings
        currentValueText.value = type.ClassDataAdapter().toUi(classData)
        newValueText.value = ""



        when (type) {
            //Handle REMOVE type

            REMOVE -> {
                if (!fromClassDataChange) {
                    removeRequest.value = Once(classData)
                }
            }
            CODE -> {

            }

            else -> {

            }
        }
    }


    fun onClickTypeSelectButton() {
        clickTypeSelect.value = Once(true)
    }

    fun onClickEditType(type: EditDTO.Type) {
        selectedType.value = type
    }

    fun onClickNaviInformationButton() {
        currentClassData.value?.let { classData ->
            clickNavigateInfo.value = Once(classData)
        }
    }

    fun onClickSubmitButton() = viewModelScope.launch {


        val classData = currentClassData.value ?: return@launch
        val type = selectedType.value ?: return@launch
        val value = newValueText.value ?: return@launch
        if (value == "") return@launch

        loading.value = true

        try {
            authRepository.getUid()?.let { uid ->

                val info = userRepository.getUserInfo(uid)
                val duplicate = checkDuplicateEditRequest(classData, type, value)

                val edit = Edit(classData, info.toEntity(), type, value)

                if (duplicate) {
                    duplicateItemExist.value = Once(edit.toDTO())
                } else {
                    createEditRequest(edit.toDTO())
                }

            }
        } catch (e: Throwable) {
            msg.value =  Once(NegativeMsg.PROFILE_INIT_FAIL)
            debugE(TAG, e)
        }

        loading.value = false
    }


    fun onClickBackButton() {
        clickBackButton.value = Once(true)
    }

    fun onClickRemoveClassButton(classData: ClassData) = viewModelScope.launch {


        loading.value = true

        authRepository.getUid()?.let { uid ->

            try {
                authRepository.getUid()?.let { uid ->

                    val info = userRepository.getUserInfo(uid)
                    val duplicate = checkDuplicateEditRequest(classData,REMOVE)


                    val edit = Edit(classData, info.toEntity(), REMOVE,"").toDTO()

                    if (duplicate) {
                        //이미 일치하는 것이 존재
                        duplicateItemExist.value =  Once(edit)
                    } else {
                        //일치하는 것이 존재하지 않음
                        createEditRequest(edit)
                    }

                }
            } catch (e: Throwable) {
                msg.value =  Once(NegativeMsg.PROFILE_INIT_FAIL)
                debugE(TAG, e)
            }


        }

        loading.value = false
    }



    /**
     * 수정 요청을 생성한다.
     */
    fun createEditRequest(editDTO: EditDTO) = viewModelScope.launch{
        try {
            val request = editRepository.createEditRequest(editDTO)

            requestSuccess.value = Once(request)
        } catch (e: Throwable) {
            msg.value =  Once(NegativeMsg.POST_FAIL_MAKE_REQUEST)
            debugE(TAG, e)
        }
    }

    /**
     * 이미 존재하는 EditRequest 중 과목과 수정 Type이 일치하는 것이 있는 지 검사한다.
     *
     * @return 동일한 것이 있으면 true, 없으면 false 를 비동기로 반환
     */
    private suspend fun checkDuplicateEditRequest(classData: ClassData, type: EditDTO.Type, value: String? = null): Boolean = withContext(IO) {
        val list = editRepository.getEditListWithCode(classData.code, Constant.CURRENT_YEAR, Constant.CURRENT_SEMESTER)
        val sameCount = list.count {
            it.editClassData.id == classData.id && it.type == type.name && it.value == (value ?: "")
        }
        return@withContext sameCount > 0
    }

}