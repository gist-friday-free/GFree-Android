package org.mjstudio.gfree.domain.entity

import android.content.res.Resources
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.mjstudio.gfree.domain.R

@Parcelize
data class TimeSlot(val week: String, val startTime: HourMin, val endTime: HourMin) : Parcelable, Entity {

    fun isDuplicate(other: TimeSlot): Boolean {
        if (this.week != other.week)
            return false

        if (this.startTime.toMin() in other.startTime.toMin() + 1..other.endTime.toMin() - 1) {
            return true
        } else if (this.endTime.toMin() in other.startTime.toMin() + 1..other.endTime.toMin() - 1)
            return true
        else if (this.startTime.toMin() == other.startTime.toMin())
            return true
        else if (this.endTime.toMin() == other.endTime.toMin())
            return true
        else if (other.startTime.toMin() in this.startTime.toMin() + 1..this.endTime.toMin() - 1) {
            return true
        } else if (other.endTime.toMin() in this.startTime.toMin() + 1..this.endTime.toMin() - 1)
            return true
        else if (other.startTime.toMin() == this.startTime.toMin())
            return true
        else if (other.endTime.toMin() == this.endTime.toMin())
            return true

        return false
    }

    companion object {
        fun getWeekColor(res: Resources, week: String): Int {
            return when (week) {
                "mon" -> { res.getColor(R.color.Monday) }
                "tue" -> { res.getColor(R.color.Tuesday) }
                "wed" -> { res.getColor(R.color.Wednesday) }
                "thu" -> { res.getColor(R.color.Thursday) }
                "fri" -> { res.getColor(R.color.Friday) }
                "sat" -> { res.getColor(R.color.Saturday) }
                "sun" -> { res.getColor(R.color.Sunday) }
                else -> throw Exception("No Weekend Color Exception")
            }
        }
        fun getWeekKoreanText(week: String): String {
            return when (week) {
                "mon" -> "월"
                "tue" -> "화"
                "wed" -> "수"
                "thu" -> "목"
                "fri" -> "금"
                "sat" -> "토"
                "sun" -> "일"
                else -> throw Exception("No Weekend Korean Exception $week")
            }
        }
    }
    fun getPeriod(): String {
        return if (startTime.hour == 9) {
            "1  "
        } else if (startTime.hour == 10) {
            "2  "
        } else if (startTime.hour == 13)
            "3  "
        else if (startTime.hour == 14)
            "4  "
        else if (startTime.hour == 16)
            "5  "
        else
            "  "
    }

    override fun toString() : String {
        return "${"%02d".format(startTime.hour)}:${"%02d".format(startTime.min)} ~ ${"%02d".format(endTime.hour)}:${"%02d".format(endTime.min)}"
    }
}