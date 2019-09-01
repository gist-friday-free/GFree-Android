package org.mjstudio.ggonggang.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import org.mjstudio.ggonggang.common.AutoClearedValue
import org.mjstudio.ggonggang.databinding.FragmentMainBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject

class MainFragment : DaggerFragment() {

    private val TAG = MainFragment::class.java.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mViewModel: MainViewModel

    private var mBinding: FragmentMainBinding by AutoClearedValue()
    private lateinit var mPagerAdapter: MainFragmentPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mViewModel = ViewModelProviders.of(activity!!, viewModelFactory)[MainViewModel::class.java]
        this.lifecycle.addObserver(mViewModel)

        mBinding = FragmentMainBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = mViewModel

        initViewPager()
        listenViewModel()

        return mBinding.root
    }

    private fun initViewPager() {
        mPagerAdapter = MainFragmentPagerAdapter(this)
        mBinding.viewPager.adapter = mPagerAdapter
        mBinding.viewPager.offscreenPageLimit = 4
        mBinding.viewPager.isUserInputEnabled = false

    }



    private fun listenViewModel() {
        mViewModel.apply {
            this.currentNavigationTab.observe(viewLifecycleOwner, Observer {
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return@Observer
                val v = activity?.currentFocus ?: return@Observer
                imm.hideSoftInputFromWindow(v.windowToken,0)
            })
        }
    }
}


fun hideKeyboard(context : Context, v : View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken,0)
}