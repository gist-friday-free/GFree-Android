package org.mjstudio.gfree.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HourMin(val hour: Int, val min: Int) : Parcelable, Entity {

    constructor(min: Int) : this(min / 60, min % 60)
    constructor(hourMin: Int, isCombination: Boolean) : this(hourMin / 100, hourMin % 100)

    fun toMin(): Int = hour * 60 + min
    fun toCombination(): Int {
        val hourString = if (hour<10) "0$hour" else "$hour"
        val minString = if (min<10) "0$min" else "$min"

        return (hourString + minString).toInt()
    }
}
