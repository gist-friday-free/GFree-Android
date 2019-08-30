package org.mjstudio.ggonggang.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel
import dagger.android.support.DaggerFragment

abstract class BaseDaggerFragment<B : ViewDataBinding, VM : ViewModel>(@LayoutRes private val layoutResourceId: Int) : DaggerFragment() {

    protected var mBinding by AutoClearedValue<B>()
    protected abstract val mViewModel: VM

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<B>(inflater, layoutResourceId, container, false).also {
            mBinding = it
        }.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.setVariable(BR.vm, mViewModel)
    }
}