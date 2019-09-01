package org.mjstudio.ggonggang.common

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.ui.auth.SNACKBAR_ACTION
import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateFormat(): String {

    val simple = SimpleDateFormat("yyyy/MM/dd")
    val date = Date(this)
    return simple.format(date)

}

fun Context.getPixel(dp: Int): Int {
    val scale = resources.displayMetrics.density
    val pixels = (dp * scale + 0.5f).toInt()

    return pixels
}

typealias SnackBarAction = (View) -> Unit

fun View.showSnackbar(msg: String, action: Pair<SNACKBAR_ACTION, SnackBarAction>? = null,
                      onShown : (() -> Unit)? = null, onDismissed :(()->Unit)? = null) {
    val snackBar = if (action == null) {
        Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
    } else {
        Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
                .setAction(action.first, action.second)
    }
    snackBar.addCallback(object : BaseCallback<Snackbar>() {

        override fun onShown(transientBottomBar: Snackbar?) {
            super.onShown(transientBottomBar)
            onShown?.invoke()
        }

        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            onDismissed?.invoke()
        }

    })
    snackBar.showWithTheme()
}
fun Snackbar.showWithTheme() {
    val res = this.view.resources
    val backColor = Color.BLACK
    val textColor = res.getColor(R.color.colorTextWhite)
    val textAccentColor = Color.YELLOW

    this.view.setBackgroundColor(backColor)

    val tv = view.findViewById(R.id.snackbar_text) as TextView
    tv.setTextColor(textColor)
    this.setActionTextColor(textAccentColor)

    show()
}

fun <T> MutableLiveData<Once<T>>.observeOnce(lifecycleOwner: LifecycleOwner, run: (T) -> Unit) {
    this.observe(lifecycleOwner, Observer {
        it?.getContentIfNotHandled()?.let { result ->
            run(result)
        }
    })
}
fun View.addGlobalLayoutOnce(f: () -> Unit) {
    this.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            this@addGlobalLayoutOnce.viewTreeObserver.removeOnGlobalLayoutListener(this as OnGlobalLayoutListener)
            f()
        }
    })
}

//infix fun <T> MutableLiveData<T>.post(value : T) {
//    this.postValue(value)
//}
