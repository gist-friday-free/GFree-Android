package org.mjstudio.ggonggang.ui.timetable

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.PositiveMsg
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import javax.inject.Inject

class TimeTableViewModel @Inject constructor(private val authRepository: FirebaseAuthRepository, private val userRepository: UserRepository) : ViewModel(), LifecycleObserver {

    private val TAG = TimeTableViewModel::class.java.simpleName

    private var firstAdd: Boolean = true

    //    val registeredClasses: MutableLiveData<List<ClassData>> = MutableLiveData(listOf())
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var currentSelectedClassData: MutableLiveData<ClassData?> = MutableLiveData(null)

    //region EVENT
    val msgEvent: MutableLiveData<Once<PositiveMsg>> = MutableLiveData()
    val errorEvent: MutableLiveData<Once<NegativeMsg>> = MutableLiveData()
    val clickItem: MutableLiveData<Once<ClassData>> = MutableLiveData()
    val navInfo: MutableLiveData<Once<ClassData>> = MutableLiveData()
    val closeMenu: MutableLiveData<Once<Boolean>> = MutableLiveData()
    val openMenu: MutableLiveData<Once<Boolean>> = MutableLiveData()
    //endregion


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        initClasses()
    }

    private fun initClasses() {
        firstAdd = false

    }

    /**
     * 유저가 과목을 등록해제하는 메서드
     */
    private fun unregisterClass(classData: ClassData) = viewModelScope.launch {


        try {
            authRepository.getUid()?.let { uid ->

                val it = userRepository.unregisterClass(uid, classData)
                //삭제 성공
                msgEvent.value = Once(PositiveMsg.CLASS_UNREGISTER_SUCCESS)
                userRepository.removeRegisteredClasDataFromCache(it)
                currentSelectedClassData.value = null

            }
        } catch (e: Throwable) {
            errorEvent.value = Once(NegativeMsg.CLASS_UNREGISTER_FAIL)
        }
    }

    fun onClickClassDataItem(item: ClassData) {
        currentSelectedClassData.value = item
        clickItem.value = Once(item)
        openMenu.value = Once(true)
    }

    fun onClickRemoveButton() {
        closeMenu.value = Once(true)
        currentSelectedClassData.value?.let {
            unregisterClass(it)
        }
    }

    fun onClickInformationButton() {
        currentSelectedClassData.value?.let {
            navInfo.value = Once(it)
        }
    }
}
