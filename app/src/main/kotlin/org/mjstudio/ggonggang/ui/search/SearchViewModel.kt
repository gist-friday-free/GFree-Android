package org.mjstudio.ggonggang.ui.search

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.ferfalk.simplesearchview.SimpleOnQueryTextListener
import com.ferfalk.simplesearchview.SimpleSearchView
import com.ferfalk.simplesearchview.SimpleSearchViewListener
import kotlinx.coroutines.launch
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.common.CustomMsg
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.PositiveMsg
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.enumerator.Major
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.ui.MainNavigationTab
import org.mjstudio.ggonggang.ui.profile.ProfileAdapter
import org.mjstudio.ggonggang.ui.MainNavigationTab.TIMETABLE
import org.mjstudio.ggonggang.util.ClassDataUtil
import javax.inject.Inject

class SearchViewModel @Inject constructor(
        private val authRepository: FirebaseAuthRepository,
        private val classDataRepository: ClassDataRepository,
        private val userRepository: UserRepository
) : ViewModel(), LifecycleObserver {
    /**
     * 이번 년도, 이번 학기에 해당하는 모든 과목의 데이터
     */
    var allClassData : List<ClassData> = listOf()
    /**
     * RecyclerView에 보일 데이터
     */
    val filteredClassData: MutableLiveData<List<ClassData>> = MutableLiveData(listOf())

    private val TAG = SearchViewModel::class.java.simpleName
    //region DATA
    lateinit var settings: SearchFilterSettings

//    var registeredClasses: List<ClassData>? = null

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    val currentSelectedClassData : MutableLiveData<ClassData?> = MutableLiveData()
    val isMenuExpanded : MutableLiveData<Boolean> = MutableLiveData(false)

    var currentQueryString : String = ""


    //endregion

    //region EVENT
    val startLayoutAnim = MutableLiveData<Once<Boolean>>()
    val navTabChanged = MutableLiveData<Once<MainNavigationTab>>()

    val openSearchView = MutableLiveData<Once<Boolean>>()
    val closeSearchView = MutableLiveData<Once<Boolean>>()
    val clickSearchButton: MutableLiveData<Once<Boolean>> = MutableLiveData()
    val clickFilterButton = MutableLiveData<Once<SearchFilterSettings>>()
    val clickItem: MutableLiveData<Once<ClassData>> = MutableLiveData()
    val longClickItem: MutableLiveData<Once<ClassData>> = MutableLiveData()

    val clickAddButton : MutableLiveData<Once<ClassData>> = MutableLiveData()
    val clickInformationButton : MutableLiveData<Once<ClassData>> = MutableLiveData()
    val msg = MutableLiveData<Once<Msg>>()
    val snackMsg = MutableLiveData<Once<Msg>>()
    //endregion

    val refreshListener : SwipeRefreshLayout.OnRefreshListener = object : OnRefreshListener {
        override fun onRefresh() {
            isLoading.value = true
            startQuery(currentQueryString)
        }
    }

    init {
        getSettingsFromSharedPreferences()
        startQuery("")
    }

    private fun getSettingsFromSharedPreferences() {
        settings = SearchFilterSettings()
    }

    private fun startQuery(query: String) = viewModelScope.launch {
        //데이터가 불러와지지 않았을 경우
        if(allClassData.isEmpty()) {

            isLoading.value = true
            try {
                val it = classDataRepository.getClassDataList(Constant.CURRENT_YEAR, Constant.CURRENT_SEMESTER)

                allClassData = it
                setFilteredClassData(query)
                startLayoutAnim.value = Once(true)

            }catch (t : Throwable) {
                msg.value = Once(NegativeMsg.CLASS_LIST_GET_FAIL)
                debugE(TAG, t)
            }finally {
                isLoading.value = false
            }

        }
        //이미 네트워크를 통해 불러와서 데이터가 있을 경우
        else {
            setFilteredClassData(query)
            isLoading.value=false
            startLayoutAnim.value =  Once(true)
        }
    }

    /**
     * 불러온 해당 년도, 학기의 모든 과목 중에 필터링 된 것들만 리스트에 뜨게 한다.
     */
    private fun setFilteredClassData(query : String) {
        filteredClassData.value = allClassData.filter {
            if (query == "")
                filterWithSettings(it)
            else
                filterWithQuery(it, query) && filterWithSettings(it)
        }
    }

    // 필터링 설정을 한 것을 기반으로 통과할 시에 true를 반환
    private fun filterWithSettings(_class: ClassData): Boolean {

        // 전공 필터
        if (settings.major != Major.NONE.code) {
            if (_class.code.take(2) != settings.major)
                return false
        }

        // 담을 수 있는 과목만
        if (settings.possible) {
            if (ClassDataUtil.checkDuplicate(userRepository.getRegisteredClassDataLiveData().value!!, _class).first)
                return false
        }

        // 학점으로 검색
        if (settings.grade) {
            val setGrade = settings.gradeValue
            if (_class.grade != setGrade)
                return false
        }

        return true
    }
    // 검색 단어를 기반으로 통과할 시에 true를 반환
    private fun filterWithQuery(_class: ClassData, query: String): Boolean {
        val _query = makeQueryForFiltering(query)
        val _name = makeQueryForFiltering(_class.name)

        val _code = makeQueryForFiltering(_class.code)
        val _professor = makeQueryForFiltering(_class.professor ?: "")

        // TODO 검색 조건 추가
        if (_name.contains(_query)) return true

        if (_code.contains(_query)) return true

        if (_professor.contains(_query)) return true

        return false
    }

    private fun makeQueryForFiltering(query: String): String {
        return query.toLowerCase().replace(" ", "").replace("\n", "").replace("'", "").replace("\"", "")
    }

    fun onClickItem(item: ClassData) {
        this.clickItem.value = Once(item)
    }

    fun onClickSearchButton() {
        clickSearchButton.value = Once(true)
    }
    fun onClickFilterButton() {
        clickFilterButton.value = Once(settings)
    }

    /**
     * 아이템의 more 버튼을 클릭
     */
    fun onClickMoreButton(item : ClassData){
        currentSelectedClassData.value = item
        isMenuExpanded.value = !(isMenuExpanded.value ?: true)
    }

    /**
     * FloatingActionMenu를 클릭
     */
    fun onClickMenuButton() {
        //닫혀있으면,
        isMenuExpanded.value = !(isMenuExpanded.value ?: true)
    }

    fun onClickAddButton() = viewModelScope.launch {
        clickAddButton.value = Once(currentSelectedClassData.value!!)


        val checkDuplicateResult = ClassDataUtil.checkDuplicate(userRepository.getRegisteredClassDataLiveData().value!!,currentSelectedClassData.value!!)
        //만약 겹친다면
        if(checkDuplicateResult.first) {
            msg.value = Once(CustomMsg("${NegativeMsg.CLASS_DUPLICATE.msg}\nwith ${checkDuplicateResult.second ?: ""}"))
        }else {

            try {
                authRepository.getUid()?.let { uid ->
                    val result = userRepository.registerClass(uid, currentSelectedClassData.value!!)
                    //추가에 성공한다면
                    userRepository.addRegisteredClassDataToCache(result)

                    isMenuExpanded.value = false
                    navTabChanged.value = Once(TIMETABLE)

                    snackMsg.value = Once(PositiveMsg.CLASS_REGISTER_SUCCESS)
                }
            }catch(e : Throwable) {
                snackMsg.value = Once(NegativeMsg.CLASS_REGISTER_FAIL)
            }finally {
                isLoading.value=false
            }




        }
    }
    fun onClickInformationButton() {
        clickInformationButton.value = Once(currentSelectedClassData.value!!)
    }

    val searchViewListener = object : SimpleSearchViewListener() {
        override fun onSearchViewShownAnimation() {
        }

        override fun onSearchViewClosed() {
        }

        override fun onSearchViewClosedAnimation() {
        }

        override fun onSearchViewShown() {
        }
    }
    val queryTextListener = object : SimpleOnQueryTextListener() {
        override fun onQueryTextSubmit(query: String?): Boolean {
            closeSearchView.value = Once(true)
            return super.onQueryTextSubmit(query)
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            currentQueryString = newText ?: ""
            msg.value = Once(CustomMsg(newText ?: ""))
            startQuery(newText ?: "")
            return super.onQueryTextChange(newText)
        }

        override fun onQueryTextCleared(): Boolean {
            return super.onQueryTextCleared()
        }
    }

    fun sholudApplySettings() {
        startQuery(currentQueryString)
    }
}

@BindingAdapter("app:classes")
fun RecyclerView.setClasses(classes: List<ClassData>) {
    (this.adapter as? SearchItemAdapter)?.let {
        it.items = classes
        it.notifyDataSetChanged()
    }

    (this.adapter as? ProfileAdapter)?.let {
        it.items = classes
        it.notifyDataSetChanged()
    }
}


@BindingAdapter("app:searchViewListener")
fun SimpleSearchView.setListener(listener : SimpleSearchViewListener) {
    this.setOnSearchViewListener(listener)
}
@BindingAdapter("app:searchViewQueryListene")
fun SimpleSearchView.setQueryListener(listener : SimpleOnQueryTextListener) {
    this.setOnQueryTextListener(listener)
}
