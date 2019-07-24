package org.mjstudio.gfree.domain.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountDTO(
    val email: String,
    val password: String
) : DTO, Parcelable