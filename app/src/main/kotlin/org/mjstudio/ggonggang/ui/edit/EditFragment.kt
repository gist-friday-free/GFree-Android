package org.mjstudio.ggonggang.ui.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.android.support.DaggerFragment
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.observeOnce
import org.mjstudio.ggonggang.common.showSnackbar
import org.mjstudio.ggonggang.common.toast
import org.mjstudio.ggonggang.databinding.FragmentEditBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject


class EditFragment : DaggerFragment() {

    private val TAG = EditFragment::class.java.simpleName

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    private lateinit var mViewModel: EditViewModel
    private lateinit var mBinding : FragmentEditBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentEditBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this,viewModelFactory).get(EditViewModel::class.java)
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner

        initRecyclerView()
        listenViewModel()
    }

    override fun onResume() {
        super.onResume()
        mBinding.recyclerView.startLayoutAnimation()
    }

    private fun initRecyclerView() {
        mBinding.recyclerView.adapter = EditAdapter(mViewModel,viewLifecycleOwner)
        mBinding.recyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
    }

    private fun listenViewModel() {
        mViewModel.apply {
            clickPostButton.observeOnce(viewLifecycleOwner) {
                findNavController().navigate(R.id.action_mainFragment_to_postFragment)
            }
            startLayoutAnim.observeOnce(viewLifecycleOwner) {
                mBinding.recyclerView.startLayoutAnimation()
            }

            msg.observeOnce(viewLifecycleOwner) {
                context?.toast(it)
            }
            snackMsg.observeOnce(viewLifecycleOwner) {
                mBinding.root.showSnackbar(it.msg)
            }
        }
    }
}

