package org.mjstudio.ggonggang.common

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.toast_custom.view.*
import org.mjstudio.gfree.domain.common.Msg

// 토스트
fun Context.toast(msg: Msg) {
    this.toast(msg.msg)
}

fun Context.toast(text: String) {

    val toast = Toast(this)

    val toastView = LayoutInflater.from(this).inflate(org.mjstudio.ggonggang.R.layout.toast_custom, null, false)

    val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
    toastView.layoutParams = lp

    toast.duration = Toast.LENGTH_SHORT

    toastView.textView_toast.text = text
    toast.view = toastView

    val yOffset = this.getPixel(100)

    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, yOffset)
    toast.show()

}

fun Fragment.toast(msg: Msg) {
    this.toast(msg.msg)
}
fun Fragment.toast(text: String) {
    activity?.toast(text)
}