package org.mjstudio.ggonggang.common

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class LeaklessRecyclerView @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null) : RecyclerView(context,attrs) {

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        this.adapter = null
    }
}
