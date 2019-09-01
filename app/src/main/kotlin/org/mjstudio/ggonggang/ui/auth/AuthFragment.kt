package org.mjstudio.ggonggang.ui.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.mjstudio.gfree.domain.common.GeneralMsg
import org.mjstudio.gfree.domain.common.simpleTimer
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.R.layout
import org.mjstudio.ggonggang.common.BaseDaggerFragment
import org.mjstudio.ggonggang.common.observeOnce
import org.mjstudio.ggonggang.common.showSnackbar
import org.mjstudio.ggonggang.common.toast
import org.mjstudio.ggonggang.databinding.FragmentAuthBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject

typealias SNACKBAR_ACTION = String
class AuthFragment : BaseDaggerFragment<FragmentAuthBinding,AuthViewModel>(layout.fragment_auth) {

	private val TAG = AuthFragment::class.java.simpleName

    companion object {
        const val ACTION_VERIFICATION: SNACKBAR_ACTION = "VERIFICATION"
        const val ACTION_PASSWORDRESET: SNACKBAR_ACTION = "PASSWORDRESET"
    }

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    override lateinit var mViewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = ViewModelProvider(activity!!, viewModelFactory).get(AuthViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

        this.lifecycle.addObserver(mViewModel)
        listenViewModel()
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

        lifecycleScope.simpleTimer(300) {
            findNavController().navigate(R.id.action_authFragment_to_mainFragment)
        }
    }

    private fun showSnackBar(msg: String, action: SNACKBAR_ACTION?) {

        if (action == null) {
            mBinding.root.showSnackbar(msg)
        } else {
            mBinding.root.showSnackbar(msg, when (action) {
                ACTION_VERIFICATION -> {
                    GeneralMsg.SEND_EMAIL.msg to { _: View ->
                        mViewModel.onSendVerificationEmail()
                    }
                }
                ACTION_PASSWORDRESET -> {
                    GeneralMsg.SEND_EMAIL.msg to { _: View ->
                        mViewModel.onSendPasswordResetEmail()
                    }
                }
                else -> throw IllegalArgumentException("NO SNACKBAR ACTION IN AUTHFRAGMENT")
            }

            )
        }


    }
}


