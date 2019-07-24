package org.mjstudio.gfree.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClassData(
    val id: Int,
    val year: Int,
    val semester: Int,
    val code: String,
    var name: String,
    val professor: String?,
    val place: String?,
    val size: Int?,
    val time: List<TimeSlot> = listOf(),
    val grade: Int?
) : Parcelable, Entity

