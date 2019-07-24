package org.mjstudio.ggonggang.ui.post


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.observeOnce
import org.mjstudio.ggonggang.common.showSnackbar
import org.mjstudio.ggonggang.common.toast
import org.mjstudio.ggonggang.databinding.FragmentPostBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import org.mjstudio.ggonggang.ui.information.InformationFragmentDirections
import javax.inject.Inject

class PostFragment : DaggerFragment() {
    private val TAG = PostFragment::class.java.simpleName

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    private lateinit var mBinding : FragmentPostBinding
    private lateinit var mViewModel : PostViewModel

    private var mDialog : PostTypeSelectBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProviders.of(this,viewModelFactory)[PostViewModel::class.java]
        debugE(TAG,viewModelFactory)
        debugE(TAG,mViewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentPostBinding.inflate(inflater,container,false)
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner

        listenViewModel()

        return mBinding.root
    }


    private fun listenViewModel() {
        mViewModel.apply {

            msg.observeOnce(viewLifecycleOwner) {
                context?.toast(it)
            }
            snackMsg.observeOnce(viewLifecycleOwner) {
                mBinding.root.showSnackbar(it.msg)
            }

            clickTypeSelect.observeOnce(viewLifecycleOwner) {
                mDialog = PostTypeSelectBottomSheet()
                mDialog?.show(childFragmentManager,mDialog?.tag)
            }
            clickNavigateInfo.observeOnce(viewLifecycleOwner) {
                val dir = InformationFragmentDirections.actionGlobalInformationFragment(it)
                findNavController().navigate(dir)
            }
            clickBackButton.observeOnce(viewLifecycleOwner) {
                findNavController().popBackStack()
            }

            selectedType.observe(viewLifecycleOwner, Observer {
                mDialog?.dismiss()
            })

            currentClassDataObserver.observe(viewLifecycleOwner, Observer {  })

            removeRequest.observeOnce(viewLifecycleOwner) { classData->
                AlertDialog.Builder(context!!)
                        .setTitle(R.string.post_remove_title)
                        .setMessage(resources.getString(R.string.post_remove_body).format(classData.name))
                        .setPositiveButton(R.string.yes) {_,_->
                            mViewModel.onClickRemoveClassButton(classData)
                        }
                        .setNegativeButton(R.string.no){_,_->

                        }
                        .show()
            }

            removeSuccess.observeOnce(viewLifecycleOwner) {
                findNavController().popBackStack()
            }

            duplicateItemExist.observeOnce(viewLifecycleOwner) {

                AlertDialog.Builder(context!!)
                        .setTitle(R.string.post_duplicate_warning_title)
                        .setMessage(resources.getString(R.string.post_duplicate_warning_body).format(it.editClassData.code,it.type,it.value))
                        .setPositiveButton(R.string.yes) { _, _->
                            mViewModel.createEditRequest(it)
                        }
                        .setNegativeButton(R.string.no) { _, _ ->

                        }
                        .show()
            }

        }
    }


}
