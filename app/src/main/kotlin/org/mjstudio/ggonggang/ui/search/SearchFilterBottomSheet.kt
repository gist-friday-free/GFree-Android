package org.mjstudio.ggonggang.ui.search

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.android.support.AndroidSupportInjection
import org.mjstudio.gfree.domain.enumerator.Major
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.databinding.SearchFilterBinding
import org.mjstudio.ggonggang.di.ViewModelFactory
import javax.inject.Inject

class SearchFilterBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val TAG = SearchFilterBottomSheet::class.java.simpleName

    private lateinit var mBinding : org.mjstudio.ggonggang.databinding.SearchFilterBinding
    private lateinit var mViewModel : SearchViewModel


    override fun getTheme(): Int = R.style.BaseBottomSheetDialog_BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(activity!!, theme)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)


        setStyle(androidx.fragment.app.DialogFragment.STYLE_NORMAL, R.style.SearchFilterDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = SearchFilterBinding.inflate(inflater,container,false)
        mBinding.lifecycleOwner = viewLifecycleOwner

        return mBinding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel = ViewModelProvider(activity!!,viewModelFactory)[SearchViewModel::class.java]
        mBinding.vm = mViewModel
        setMajorSpinner()
    }

    private fun setMajorSpinner() {
        val adapter = SearchSpinnerAdapter(context!!)
        mBinding.searchFilterMajor.adapter = adapter
        mBinding.searchFilterMajor.setSelection(adapter.getIndexWithItemName(mViewModel.settings.major))
        mBinding.searchFilterMajor.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mViewModel.settings.major = Major.getMajorsForSearchFilter()[position].code
            }
        }
    }



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mViewModel.sholudApplySettings()
    }
}