package org.mjstudio.ggonggang.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_setting.*
import org.mjstudio.gfree.domain.constant.Constant.SETTING_NOTICE
import org.mjstudio.ggonggang.application.sp
import org.mjstudio.ggonggang.common.AutoClearedValue
import org.mjstudio.ggonggang.databinding.FragmentSettingBinding
import org.mjstudio.ggonggang.util.NotificationTopic

class SettingFragment : Fragment() {
    private var mBinding: org.mjstudio.ggonggang.databinding.FragmentSettingBinding by AutoClearedValue()
    private val TAG = SettingFragment::class.java.simpleName

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentSettingBinding.inflate(inflater, container, false)
        mBinding.setLifecycleOwner(viewLifecycleOwner)
        initViews()
        return mBinding.root
    }

    private fun initViews() {
        val switchNotice = switch_setting_notice_noti

        val noticeMode = sp.loadBoolean(SETTING_NOTICE)

        switchNotice.isChecked = noticeMode ?: false

        switchNotice.setOnCheckedChangeListener { buttonView, isChecked ->
            sp.saveBoolean(SETTING_NOTICE, isChecked)
            if (isChecked) {
                FirebaseMessaging.getInstance().subscribeToTopic(NotificationTopic.NOTICE.topic)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(NotificationTopic.NOTICE.topic)
            }
        }
    }
}
