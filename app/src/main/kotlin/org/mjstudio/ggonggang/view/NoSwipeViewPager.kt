package org.mjstudio.ggonggang.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoSwipeViewPager @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null
) : ViewPager(context,attributeSet) {
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }
}