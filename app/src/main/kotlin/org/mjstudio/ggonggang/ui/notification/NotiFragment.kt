package org.mjstudio.ggonggang.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.android.support.DaggerFragment
import org.mjstudio.ggonggang.databinding.FragmentNotiBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject


class NotiFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    private lateinit var mViewModel: NotiViewModel
    private lateinit var mBinding : FragmentNotiBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentNotiBinding.inflate(inflater,container,false)

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this,viewModelFactory).get(NotiViewModel::class.java)
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner

        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        mBinding.recyclerViewNoti.startLayoutAnimation()
    }

    private fun setRecyclerView() {
        mBinding.recyclerViewNoti.addItemDecoration(DividerItemDecoration(context!!,DividerItemDecoration.VERTICAL).apply {
        })
    }

}
