package org.mjstudio.ggonggang.common

import android.content.Context
import android.content.res.Resources
import android.graphics.PorterDuff
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("android:visibility")
fun View.setVisibility(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}
@BindingAdapter("android:textColor")
fun TextView.setTextColor(@ColorRes resId: Int) {
    this.setTextColor(resources.getColor(context, resId))
}

@Suppress("DEPRECATION")
fun Resources.getColor(context: Context, resId: Int): Int {
    return if (VERSION.SDK_INT >= VERSION_CODES.M) {
        this.getColor(resId, context.theme)
    } else {
        this.getColor(resId)
    }
}
@BindingAdapter("app:filterColor")
fun ImageButton.setFilterColor(resId : Int?) {
    if(resId == null)
        this.clearColorFilter()
    else
        this.setColorFilter(resId, PorterDuff.Mode.SRC_ATOP)
}

@BindingAdapter("android:text")
fun TextView.setAny(any : Any) {
    this.setText(any.toString())
}

@BindingAdapter("app:iconRotation")
fun View.setIconRotation(rotation : Int) {
    this.animate().rotation(rotation.toFloat()).apply {
        duration = 250
        start()
    }
}
@BindingAdapter("app:enable")
fun View.setEnableBinding(enable : Boolean) {
    this.isEnabled = enable
}

@BindingAdapter("app:onRefresh")
fun SwipeRefreshLayout.setRefreshListener(listener : SwipeRefreshLayout.OnRefreshListener) {
    this.setOnRefreshListener(listener)
}

@BindingAdapter("app:refreshing")
fun SwipeRefreshLayout.setRefreshState(refresh : Boolean) {
    this.isRefreshing = refresh
}

@BindingAdapter("app:adapter")
fun RecyclerView.setAdapterBinding(adapter : RecyclerView.Adapter<ViewHolder>) {
    this.adapter = adapter
}
@BindingAdapter("app:decoration")
fun RecyclerView.setItemDecoration(decoration: ItemDecoration) {
    this.addItemDecoration(decoration)
}