package org.mjstudio.ggonggang.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import org.mjstudio.gfree.domain.common.GeneralMsg
import org.mjstudio.gfree.domain.common.rxSingleTimer
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.observeOnce
import org.mjstudio.ggonggang.common.showSnackbar
import org.mjstudio.ggonggang.common.toast
import org.mjstudio.ggonggang.databinding.FragmentAuthBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject

typealias SNACKBAR_ACTION = String
class AuthFragment : DaggerFragment() {
    private val TAG = AuthFragment::class.java.simpleName

    companion object {
        const val ACTION_VERIFICATION: SNACKBAR_ACTION = "VERIFICATION"
        const val ACTION_PASSWORDRESET: SNACKBAR_ACTION = "PASSWORDRESET"
    }

    private lateinit var mBinding: FragmentAuthBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mViewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewModel = ViewModelProviders.of(activity!!, viewModelFactory)[AuthViewModel::class.java]
        this.lifecycle.addObserver(mViewModel)

        mBinding = FragmentAuthBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = mViewModel

        listenViewModel()

        return mBinding.root
    }
    private fun listenViewModel() {
        mViewModel.apply {
            msg.observeOnce(viewLifecycleOwner) {
                toast(it.msg)
            }
            snackMsg.observeOnce(viewLifecycleOwner) {
                showSnackBar(it.first.msg, it.second)
            }
            navigateMainFragment.observeOnce(viewLifecycleOwner) {
                startMainFragment()
            }
            showSignUpDialog.observeOnce(viewLifecycleOwner) { account ->
                val direction = AuthFragmentDirections.actionAuthFragmentToSignUpDialogFragment(account)
                findNavController().navigate(direction)
            }
        }
    }

    private fun startMainFragment() {
        rxSingleTimer(300) {
            findNavController().navigate(R.id.action_authFragment_to_mainFragment)
        }
    }

    private fun showSnackBar(msg: String, action: SNACKBAR_ACTION?) {

        if (action == null) {
            mBinding.root.showSnackbar(msg)
        } else {
            mBinding.root.showSnackbar(msg,
                    when (action) {
                        ACTION_VERIFICATION -> {
                            GeneralMsg.SEND_EMAIL.msg to { _: View->
                                mViewModel.sendVerificationEmail()
                            }
                        }
                        ACTION_PASSWORDRESET -> {
                            GeneralMsg.SEND_EMAIL.msg to { _: View->
                                mViewModel.sendPasswordResetEmail()
                            }
                        }
                        else -> throw IllegalArgumentException("NO SNACKBAR ACTION IN AUTHFRAGMENT")
                    }

            )
        }
    }
}
