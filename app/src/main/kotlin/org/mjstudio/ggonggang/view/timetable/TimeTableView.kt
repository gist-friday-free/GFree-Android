package org.mjstudio.ggonggang.view.timetable

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import me.grantland.widget.AutofitTextView
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.application.screenWidth
import org.mjstudio.ggonggang.common.getPixel
import org.mjstudio.ggonggang.ui.timetable.TimeTableViewModel
import kotlin.properties.Delegates
import kotlin.properties.Delegates.notNull

class TimeTableView(context: Context, val classData: ClassData, timeIndex: Int, bgColor: Int) : FrameLayout(context) {

    private val TAG = TimeTableView::class.java.simpleName

    private lateinit var mViewModel: TimeTableViewModel

    var week: String by Delegates.notNull()
    var startHour: Int by Delegates.notNull()
    var startMin: Int by Delegates.notNull()
    var endHour: Int by Delegates.notNull()
    var endMin: Int by Delegates.notNull()
    var durationMin: Int by notNull()

    var calculatedHeight: Int by notNull()
    var cellWidth: Int by notNull()

    var backgroundColor: Int? = bgColor

    init {
        val time = classData.time[timeIndex]
        startHour = time.startTime.hour
        startMin = time.startTime.min
        endHour = time.endTime.hour
        endMin = time.endTime.min
        week = time.week
        durationMin = endHour * 60 + endMin - startHour * 60 - startMin
        initView()
    }

    fun bindViewModel(vm: TimeTableViewModel) {
        this.mViewModel = vm

        this.setOnClickListener {
            mViewModel.onClickClassDataItem(classData)
        }
    }

    private fun initView() {
        setBackgroundColor(backgroundColor!!)
        setPadding(0, 0, 0, 0)

        val button = AutofitTextView(context)
        button.setMinTextSize(8)
        val lp = androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        button.gravity = Gravity.CENTER_HORIZONTAL
        button.layoutParams = lp
        button.text = classData.name
        button.background = null
        button.setTextColor(Color.WHITE)
        button.textSize = 13.toFloat()

        button.setShadowLayer(0.5f,0f,0f,resources.getColor(R.color.textShadow))

        addView(button)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        calculatedHeight = Math.round(context.getPixel(durationMin).toFloat())
        val timeWidth=resources.getDimension(R.dimen.timetable_time_width)
        cellWidth = ((screenWidth - timeWidth)  / 5).toInt()

        val sidePadding = context.getPixel(3).toInt()

        setPadding(sidePadding, 0, sidePadding, 0)

        setMeasuredDimension(cellWidth, calculatedHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val margin = context.getPixel(2).toInt()
        for (i in 0 until childCount) {
            getChildAt(i).layout(margin, margin, width - margin*2, height - margin*2)
        }
    }
}