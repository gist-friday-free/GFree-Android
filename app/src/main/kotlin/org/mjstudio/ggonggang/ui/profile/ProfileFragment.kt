package org.mjstudio.ggonggang.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.PagerSnapHelper
import dagger.android.support.DaggerFragment
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.common.AutoClearedValue
import org.mjstudio.ggonggang.databinding.FragmentProfileBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import org.mjstudio.ggonggang.widget.OnSnapPositionChangeListener
import org.mjstudio.ggonggang.widget.SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL
import org.mjstudio.ggonggang.widget.attachSnapHelperWithListener
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    lateinit var mViewModel : ProfileViewModel

    private var mBinding: FragmentProfileBinding by AutoClearedValue()
    private val TAG = ProfileFragment::class.java.simpleName


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mViewModel = ViewModelProviders.of(this,viewModelFactory)[ProfileViewModel::class.java]


        mBinding = FragmentProfileBinding.inflate(inflater, container, false).apply {
            vm = mViewModel
            this.userRepository = this@ProfileFragment.userRepository
            lifecycleOwner = viewLifecycleOwner
        }


        initView()

        return mBinding.root
    }


    private fun initView() {
        mBinding.recyclerViewProfile.attachSnapHelperWithListener(
                PagerSnapHelper(),
                NOTIFY_ON_SCROLL,
                object : OnSnapPositionChangeListener {
                    override fun onSnapPositionChange(position: Int) {
                        debugE("position : $position")
                    }
                }
        )
    }

}
