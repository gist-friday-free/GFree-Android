package org.mjstudio.gfree.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notice(
    val id: Int,
    val title: String,
    val subtitle: String,
    val body: String,
    val time: Long,
    val writer: String
) : Parcelable, Entity