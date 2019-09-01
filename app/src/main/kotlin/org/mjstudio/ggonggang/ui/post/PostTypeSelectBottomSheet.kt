package org.mjstudio.ggonggang.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.ggonggang.databinding.DialogPostTypeBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject

class PostTypeSelectBottomSheet : BottomSheetDialogFragment() {

    private val TAG = PostTypeSelectBottomSheet::class.java.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mBinding : DialogPostTypeBinding
    private lateinit var mViewModel : PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(this,viewModelFactory)[PostViewModel::class.java]
        debugE(TAG,viewModelFactory)
        debugE(TAG,mViewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DialogPostTypeBinding.inflate(inflater,container,false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = mViewModel

        return mBinding.root
    }
}