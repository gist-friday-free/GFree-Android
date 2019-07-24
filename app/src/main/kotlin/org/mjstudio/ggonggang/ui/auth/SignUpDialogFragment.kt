package org.mjstudio.ggonggang.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerDialogFragment
import org.mjstudio.ggonggang.common.observeOnce
import org.mjstudio.ggonggang.databinding.DialogSignupBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject

class SignUpDialogFragment : DaggerDialogFragment() {
    private val TAG = SignUpDialogFragment::class.java.simpleName

    private lateinit var mBinding: org.mjstudio.ggonggang.databinding.DialogSignupBinding
    private lateinit var mViewModel: AuthViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewModel = ViewModelProviders.of(activity!!, viewModelFactory)[AuthViewModel::class.java]

        mBinding = DialogSignupBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = mViewModel

        listenViewModel()

        return mBinding.root
    }

    private fun listenViewModel() {
        mViewModel.apply {
            signUpCancel.observeOnce(viewLifecycleOwner) {
                findNavController().popBackStack()
            }
            signUpSubmit.observeOnce(viewLifecycleOwner) {
                findNavController().popBackStack()
            }
        }
    }
}