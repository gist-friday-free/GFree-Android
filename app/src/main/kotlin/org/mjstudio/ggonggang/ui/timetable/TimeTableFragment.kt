package org.mjstudio.ggonggang.ui.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.AutoClearedValue
import org.mjstudio.ggonggang.common.observeOnce
import org.mjstudio.ggonggang.common.toast
import org.mjstudio.ggonggang.databinding.FragmentTimetableBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import org.mjstudio.ggonggang.ui.MainFragmentDirections
import javax.inject.Inject

class TimeTableFragment : DaggerFragment() {

    private val TAG = TimeTableFragment::class.java.simpleName

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mViewModel: TimeTableViewModel
    private var mBinding: FragmentTimetableBinding by AutoClearedValue()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewModel = ViewModelProviders.of(this, viewModelFactory)[TimeTableViewModel::class.java]
        this.lifecycle.addObserver(mViewModel)

        mBinding = FragmentTimetableBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = mViewModel
        mBinding.userRepository = this@TimeTableFragment.userRepository
        mBinding.timeTableLayout.bindViewModel(mViewModel)

        initView()
        listenViewModel()

        return mBinding.root
    }

    private fun initView() {
        mBinding.floatingButtonRevmoe.isEnabled=false
        mBinding.floatingButtonInformation.isEnabled=false
    }

    private fun listenViewModel() {

        mViewModel.apply {
            errorEvent.observe(viewLifecycleOwner, Observer {
                it?.getContentIfNotHandled()?.let {
                    toast(it.msg)
                }
            })
            msgEvent.observe(viewLifecycleOwner, Observer {
                it?.getContentIfNotHandled()?.let {
                    toast(it.msg)
                }
            })

            clickItem.observeOnce(viewLifecycleOwner) {
                mBinding.floatingMenu.menuButtonLabelText = it.name
                mBinding.floatingButtonRevmoe.isEnabled=true
                mBinding.floatingButtonInformation.isEnabled=true

                if(mViewModel.currentSelectedClassData.value == it)
                    mBinding.floatingMenu.close(true)
            }
            navInfo.observeOnce(viewLifecycleOwner) {classData->
                val extras= FragmentNavigatorExtras(
                        //                        mBinding.appbar to "toolbar",
                        mBinding.floatingButtonRevmoe to "graph3",
                        mBinding.floatingButtonInformation to "graph4"
                )
                val direction = MainFragmentDirections.actionMainFragmentToInformationFragment(classData)
                findNavController().navigate(direction,extras)
            }
            openMenu.observeOnce(viewLifecycleOwner) {
                mBinding.floatingMenu.open(true)
            }
            closeMenu.observeOnce(viewLifecycleOwner) {
                mBinding.floatingMenu.close(true)
            }
            currentSelectedClassData.observe(viewLifecycleOwner, Observer {
                if(it == null) {
                    mBinding.floatingMenu.menuButtonLabelText = resources.getString(R.string.fab_menu_title_hint)
                    mBinding.floatingButtonRevmoe.isEnabled=false
                    mBinding.floatingButtonInformation.isEnabled=false
                }
            })
        }
    }


}