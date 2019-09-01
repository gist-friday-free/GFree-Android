package org.mjstudio.ggonggang.ui

import android.view.MenuItem
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.reflect.full.primaryConstructor

class MainFragmentPagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment.childFragmentManager,fragment.viewLifecycleOwner.lifecycle) {
    override fun getItemCount() = MainNavigationTab.getAllTab().size

    override fun createFragment(position: Int): Fragment {
        val tab = MainNavigationTab.getTabInfoWithIndex(position)
        val kClass = tab.fragClass
        val constructor = kClass.primaryConstructor

        return constructor!!.call()
    }
}

@BindingAdapter("app:bottomTabChanged")
fun BottomNavigationView.setBottomTabChangedListener(listener: (MenuItem) -> Boolean) {
    this.setOnNavigationItemSelectedListener(listener)
}
@BindingAdapter("app:viewPagerChanged")
fun ViewPager2.setPageChangeListener(listener : ViewPager2.OnPageChangeCallback){
    this.registerOnPageChangeCallback(listener)
}
@BindingAdapter("app:viewPagerTab")
fun ViewPager2.setPageTab(tab: MainNavigationTab) {

    val idx = MainNavigationTab.getIndex(tab)

    this.setCurrentItem(idx, false)
}

@BindingMethods(BindingMethod(
        type = BottomNavigationView::class,
        attribute = "app:bottomTabId",
        method="setSelectedItemId"
))
class BottomNavigationViewBinding
