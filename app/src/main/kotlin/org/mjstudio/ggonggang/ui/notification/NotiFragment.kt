package org.mjstudio.ggonggang.ui.notification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.BaseDaggerFragment
import org.mjstudio.ggonggang.databinding.FragmentNotiBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject


class NotiFragment : BaseDaggerFragment<FragmentNotiBinding,NotiViewModel>(R.layout.fragment_noti) {

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    override lateinit var mViewModel: NotiViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = ViewModelProviders.of(this,viewModelFactory).get(NotiViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        mBinding.recyclerViewNoti.startLayoutAnimation()
    }

    private fun setRecyclerView() {
        mBinding.recyclerViewNoti.adapter = NotiAdapter()

        mBinding.recyclerViewNoti.addItemDecoration(DividerItemDecoration(context!!,DividerItemDecoration.VERTICAL).apply {
        })
    }

}
