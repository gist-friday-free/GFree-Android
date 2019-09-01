package org.mjstudio.ggonggang.ui

import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.ui.MainNavigationTab.TIMETABLE
import org.mjstudio.ggonggang.ui.edit.EditFragment
import org.mjstudio.ggonggang.ui.notification.NotiFragment
import org.mjstudio.ggonggang.ui.profile.ProfileFragment
import org.mjstudio.ggonggang.ui.search.SearchFragment
import org.mjstudio.ggonggang.ui.timetable.TimeTableFragment
import javax.inject.Inject
import kotlin.reflect.KClass

class MainViewModel @Inject constructor(
        private val classDataRepository: ClassDataRepository,
        private val authRepository: FirebaseAuthRepository,
        private val userRepository: UserRepository
) : ViewModel(), LifecycleObserver {
    private val TAG = MainViewModel::class.java.simpleName


    val currentNavigationTab: MutableLiveData<MainNavigationTab> = MutableLiveData(TIMETABLE)
    val currentTabId = Transformations.map(currentNavigationTab) {
        it.itemId
    }
        val pageChangeListener: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {

                val newTab = MainNavigationTab.getTabInfoWithIndex(position)

                if(currentNavigationTab.value != newTab)
                    currentNavigationTab.value = newTab
            }
        }
    val tabChangedListener: (MenuItem) -> Boolean = {

        val tab = MainNavigationTab.getTabInfoWithItemId(it.itemId)

        if (currentNavigationTab.value != tab)
            currentNavigationTab.value = tab
        true
    }

    init {
        // 현재 유저가 등록된 클래스를 가져온다.
        viewModelScope.launch {
            authRepository.getUid()?.let {uid->
                userRepository.getClassesWithUid(uid,Constant.CURRENT_YEAR,Constant.CURRENT_SEMESTER)
            }
        }
    }
}

enum class MainNavigationTab(val fragClass: KClass<out Fragment>, val index: Int, @IdRes val itemId: Int) {
    TIMETABLE(TimeTableFragment::class, 0, R.id.menu_timetable),
    SEARCH(SearchFragment::class, 1, R.id.menu_search),
    EDIT(EditFragment::class,2,R.id.menu_edit),
    NOTIFICATION(NotiFragment::class,3,R.id.menu_notice),
    PROFILE(ProfileFragment::class, 4, R.id.menu_profile),

    ;

    companion object {

        fun getAllTab(): List<MainNavigationTab> {
            return listOf(TIMETABLE, SEARCH, EDIT, NOTIFICATION, PROFILE)
        }

        fun getTabInfoWithIndex(index: Int): MainNavigationTab {
            return getAllTab().first {
                it.index == index
            }
        }

        fun getTabInfoWithItemId(@IdRes itemId: Int): MainNavigationTab {
            return getAllTab().first {
                it.itemId == itemId
            }
        }

        fun getIndex(tab: MainNavigationTab): Int {
            return getAllTab().indexOf(tab)
        }
    }

}