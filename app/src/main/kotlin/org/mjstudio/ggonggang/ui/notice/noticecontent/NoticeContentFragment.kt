package org.mjstudio.ggonggang.ui.notice.noticecontent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.mjstudio.gfree.domain.entity.Notice
import org.mjstudio.ggonggang.common.AutoClearedValue
import org.mjstudio.ggonggang.databinding.FragmentNoticeContentBinding
import java.text.SimpleDateFormat
import java.util.*

class NoticeContentFragment : Fragment() {
    private val args: NoticeContentFragmentArgs by navArgs()
    private lateinit var mNoticeItem: Notice
    private var mBinding: FragmentNoticeContentBinding by AutoClearedValue<FragmentNoticeContentBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentNoticeContentBinding.inflate(inflater, container, false)
        mBinding.setLifecycleOwner(viewLifecycleOwner)
        mNoticeItem = args.notice
        initView()

        return mBinding.root
    }

    private fun initView() {
        mBinding.textViewNoticeTitle.text = mNoticeItem.title

        val simple = SimpleDateFormat("yyyy-MM-DD HH:mm")

        mBinding.textViewNoticeTime.text = simple.format(Date(mNoticeItem.time))
        mBinding.textViewNoticeBody.text = mNoticeItem.body
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}
