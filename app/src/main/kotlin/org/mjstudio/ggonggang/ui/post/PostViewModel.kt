package org.mjstudio.ggonggang.ui.post

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.zipWith
import org.mjstudio.gfree.domain.adapter.toDTO
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.common.disposedBy
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
import org.mjstudio.ggonggang.common.post
import javax.inject.Inject

class PostViewModel @Inject constructor(
        private val app:  Application,
        private val classDataRepository: ClassDataRepository,
        private val editRepository: EditRepository,
        private val userRepository: UserRepository,
        private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    private val TAG = PostViewModel::class.java.simpleName

    private var currentClassDataDisposable : Disposable? = null
    private val compositeDisposable = CompositeDisposable()

    //region DATA

    /**
     * 현재 EditText에 표시되는 글씨
     */
    val codeText = MutableLiveData("")

    /**
     * EditText를 관찰하며 현재 선택된 ClassData를 변경해주는 옵저버
     */
    val currentClassDataObserver = Transformations.map(codeText) {code->
        currentClassDataDisposable?.dispose()
        val d = classDataRepository.getClassDataWithCode(code.toUpperCase(), Constant.CURRENT_YEAR,Constant.CURRENT_SEMESTER)
                .addSchedulers()
                .subscribe({
                    currentClassData.value = it.toEntity()
                }, {
                    currentClassData.value = null
                    debugE(TAG,"cannot find class With Code")
                })
        currentClassDataDisposable = d

        null
    }
    /**
     * 현재 선택된 ClassData
     */
    val currentClassData : MutableLiveData<ClassData?> = MutableLiveData(null)

    /**
     * 현재의 EditType
     */
    val selectedType = MutableLiveData<EditDTO.Type?>(null)

    /**
     * EditType 으로 표시되는 텍스트
     */
    val typeText = Transformations.map(selectedType) {
        if(it == null)
            return@map ""

        app.resources.getString(it.strResId)
    }

    /**
     * 현재 과목의 EditType에 해당하는 값 텍스트, Inverse Binding
     */
    val currentValueText = MediatorLiveData<String>().apply {
        this.addSource(currentClassData) {classData->
            classData ?: return@addSource

            synchronized(currentClassData) {
                if (selectedType.value != null) {
                    setViewConstraintsWithCurrentClassDataAndEditType(classData,selectedType.value!!,true)
                }
            }
        }
        this.addSource(selectedType) {type->
            type ?: return@addSource

            synchronized(currentClassData) {
                if (currentClassData.value != null) {
                    setViewConstraintsWithCurrentClassDataAndEditType(currentClassData.value!!,type,false)
                }
            }
        }

        this.value = ""
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
    val classInfoText: LiveData<String> = Transformations.map(currentClassData) { classData->
        if(classData == null) {
            return@map "Cannot find"
        }else {
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
    val removeSuccess = MutableLiveData<Once<EditDTO>>()

    val duplicateItemExist = MutableLiveData<Once<EditDTO>>()

    //endregion

    //region STATE
    val loading = MutableLiveData<Boolean>(false)
    //endregion

    private fun setViewConstraintsWithCurrentClassDataAndEditType(classData : ClassData, type : EditDTO.Type, fromClassDataChange : Boolean) {

        //EditText Visibility settings
        editTextVisibility.value = when(type) {
            ADD, REMOVE, TIMEADD, TIMEREMOVE ->false
            CODE, NAME, GRADE, PROFESSOR, PLACE, SIZE ->true
        }

        //EditText Text settings
        currentValueText.value=type.ClassDataAdapter().toUi(classData)
        newValueText.value = ""



        when(type) {
            //Handle REMOVE type

            REMOVE -> {
                if(!fromClassDataChange) {
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

    fun onClickEditType(type : EditDTO.Type) {
        selectedType.value = type
    }

    fun onClickNaviInformationButton() {
        currentClassData.value?.let {classData->
            clickNavigateInfo.value = Once(classData)
        }
    }

    fun onClickSubmitButton() {


        val classData = currentClassData.value ?: return
        val type = selectedType.value ?: return
        val value = newValueText.value ?: return
        if(value == "") return

        this.loading.value = true

        authRepository.getUid()?.let {uid->
            userRepository.getUserInfo(uid)
                    .zipWith(checkDuplicateEditRequest(classData,type,value))
                    .addSchedulers()
                    .doAfterTerminate { loading.value = false }
                    .subscribe({(userInfo, duplicated) ->

                        val edit = Edit(classData,userInfo.toEntity(),type,value).toDTO()

                        if(duplicated) {
                            duplicateItemExist post Once(edit)
                        }else {
                            createEditRequest(edit)
                        }

                    }, {
                        msg post Once(NegativeMsg.PROFILE_INIT_FAIL)
                        debugE(TAG,it)
                    }).disposedBy(compositeDisposable)
        }
    }


    fun onClickBackButton() {
        clickBackButton.value = Once(true)
    }
    fun onClickRemoveClassButton(classData : ClassData) {

        authRepository.getUid()?.let {uid->
            userRepository.getUserInfo(uid)
                    .zipWith(checkDuplicateEditRequest(classData,REMOVE))
                    .addSchedulers()
                    .subscribe({(userInfoDTO,existDuplicateItem)->

                        val editDTO = Edit(classData,userInfoDTO.toEntity(),EditDTO.Type.REMOVE,"").toDTO()

                        if (existDuplicateItem) {
                            //이미 일치하는 것이 존재
                            duplicateItemExist post Once(editDTO)
                        } else {
                            //일치하는 것이 존재하지 않음
                            createEditRequest(editDTO)
                        }
                    }, {
                        msg post Once(NegativeMsg.PROFILE_INIT_FAIL)
                        debugE(TAG,it)
                    }).disposedBy(compositeDisposable)
        }
    }

    /**
     * 수정 요청을 생성한다.
     */
    fun createEditRequest(editDTO: EditDTO) {
        compositeDisposable+=editRepository.createEditRequest(editDTO).addSchedulers()
                .subscribe({
                    removeSuccess post Once(it)
                }, {
                    msg post Once(NegativeMsg.POST_FAIL_MAKE_REQUEST)
                    debugE(TAG,it)
                })
    }

    /**
     * 이미 존재하는 EditRequest 중 과목과 수정 Type이 일치하는 것이 있는 지 검사한다.
     *
     * @return 동일한 것이 있으면 true, 없으면 false 를 비동기로 반환
     */
    private fun checkDuplicateEditRequest(classData : ClassData ,type : EditDTO.Type,value : String? = null) : Single<Boolean> {

        return Single.create<Boolean> { emitter->
            compositeDisposable+=editRepository.getEditListWithCode(classData.code,Constant.CURRENT_YEAR,Constant.CURRENT_SEMESTER)
                    .addSchedulers()
                    .subscribe({editDTOList->

                        val sameCount=editDTOList.count {
                            it.editClassData.id == classData.id && it.type == type.name && it.value == (value ?: "")
                        }
                        if(sameCount > 0)
                            emitter.onSuccess(true)
                        else
                            emitter.onSuccess(false)

                    }, {
                        emitter.onError(it)
                    })

        }


    }

    override fun onCleared() {
        super.onCleared()

        currentClassDataDisposable?.dispose()
        compositeDisposable.clear()
    }
}