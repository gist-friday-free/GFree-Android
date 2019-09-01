package org.mjstudio.ggonggang.ui.information

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.ChangeClipBounds
import androidx.transition.TransitionInflater
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.AutoClearedValue
import org.mjstudio.ggonggang.common.observeOnce
import org.mjstudio.ggonggang.common.showSnackbar
import org.mjstudio.ggonggang.common.toast
import org.mjstudio.ggonggang.databinding.FragmentInformationBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import org.mjstudio.ggonggang.ui.MainViewModel
import javax.inject.Inject

class InformationFragment : Fragment()  {
    private val TAG = InformationFragment::class.java.simpleName

    companion object {
        const val KEY = "CLASSDATA"
    }
    private val args: InformationFragmentArgs by navArgs()
    @Inject
    lateinit var viewModelFactory : InformationViewModelFactory

    private var mBinding: FragmentInformationBinding by AutoClearedValue()
    private lateinit var mViewModel : InformationViewModel
    lateinit var classData: ClassData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        postponeEnterTransition();
        sharedElementEnterTransition=TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = null


        if (savedInstanceState == null) {
            classData = args.classData
        } else {
            classData = savedInstanceState.getParcelable(KEY)!!
        }

        AndroidSupportInjection.inject(this)
        mViewModel = ViewModelProvider(this,viewModelFactory)[InformationViewModel::class.java]

        mBinding = FragmentInformationBinding.inflate(inflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = mViewModel

        initRecylcerView()
        listenViewModel()


        startPostponedEnterTransition()


        return mBinding.root
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY, classData)
    }

    private fun listenViewModel() {
        mViewModel.apply {
            clickBack.observeOnce(viewLifecycleOwner) {
                findNavController().popBackStack()
            }
            msg.observeOnce(viewLifecycleOwner) {
                this@InformationFragment.toast(it)
            }
            snackMsg.observeOnce(viewLifecycleOwner) {
                mBinding.root.showSnackbar(it.msg)
            }
        }
    }


    private fun initRecylcerView() {

        val layoutManager = LinearLayoutManager(context!!)
        val decorator = DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL)

        mBinding.recyclerViewReview.adapter = InformationAdapter(mViewModel)
        mBinding.recyclerViewReview.layoutManager = layoutManager
        mBinding.recyclerViewReview.addItemDecoration(decorator)
    }
}