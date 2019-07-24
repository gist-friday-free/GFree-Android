package org.mjstudio.ggonggang.view.timetable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.BindingAdapter
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.enumerator.Major
import org.mjstudio.gfree.domain.enumerator.Major.Companion.getColorFromMajorAndIndex
import org.mjstudio.ggonggang.common.getPixel
import org.mjstudio.ggonggang.ui.timetable.TimeTableViewModel

class TimeTableCoordinatorLayout
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : CoordinatorLayout(context, attrs) {

    private lateinit var mViewModel: TimeTableViewModel
    private val TAG = TimeTableCoordinatorLayout::class.java.simpleName



    fun addTimeTableView(classData: ClassData, index: Int) {
        val major = Major.getMajorWithCode(classData.code)
        major ?: return

        for (i in 0 until classData.time.size) {

            val color = getColorFromMajorAndIndex(context,major,index)

            val view = TimeTableView(context, classData, i, color)
            view.bindViewModel(mViewModel)

            addView(view)
        }
    }
    fun bindViewModel(vm: TimeTableViewModel) {
        this.mViewModel = vm

        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i) as TimeTableView
            child.bindViewModel(mViewModel)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        for (i in 0 until childCount) {
            val child = getChildAt(i) as TimeTableView

            if (child.visibility == View.GONE)
                continue

            val horizontalChange = when (child.week) {
                "mon" -> 0
                "tue" -> measuredWidth / 5
                "wed" -> measuredWidth / 5 * 2
                "thu" -> measuredWidth / 5 * 3
                "fri" -> measuredWidth / 5 * 4
                else -> 0
            }

            val childLeft = paddingLeft + horizontalChange
            val childTop = Math.round(context.getPixel((child.startHour - 9)*60 + child.startMin).toFloat())

            val childBottom = childTop + child.calculatedHeight
            val childRight = childLeft + child.cellWidth

            child.layout(childLeft, childTop, childRight, childBottom)
        }
    }
}

@BindingAdapter("app:timeTableItems")
fun TimeTableCoordinatorLayout.setChildViews(list: List<ClassData>) {
    this.removeAllViews()


    for (classData in list) {
        val major = Major.getMajorWithCode(classData.code) ?: continue
        var sameCount = 0

        var codeList: List<String> = listOf()
        for (i in 0 until this.childCount) {
            val view = this.getChildAt(i) as TimeTableView
            codeList += view.classData.code
        }
        codeList = codeList.distinct()

        for (existCode in codeList) {
            val childMajor = Major.getMajorWithCode(existCode) ?: continue

            if (major.isCommon && childMajor.isCommon) {
                sameCount += 1
            } else {
                if (major == childMajor) {
                    sameCount += 1
                }
            }
        }
        // 여러개의 TimeTableView 가 TimeTableCoordinatorLayout에 추가된다.
        this.addTimeTableView(classData, sameCount + 1)
    }
}