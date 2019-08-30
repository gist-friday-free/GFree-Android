package org.mjstudio.ggonggang.ui.edit

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.R.layout
import org.mjstudio.ggonggang.common.BaseDaggerFragment
import org.mjstudio.ggonggang.common.observeOnce
import org.mjstudio.ggonggang.common.showSnackbar
import org.mjstudio.ggonggang.common.toast
import org.mjstudio.ggonggang.databinding.FragmentEditBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject

class EditFragment : BaseDaggerFragment<FragmentEditBinding,EditViewModel>(layout.fragment_edit) {

	private val TAG = EditFragment::class.java.simpleName

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    override lateinit var mViewModel: EditViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = ViewModelProviders.of(this,viewModelFactory).get(EditViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

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


