package org.mjstudio.gfree.domain.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class NoticeDTO(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("body")
    val body: String,
    @SerializedName("created")
    val created: Long,
    @SerializedName("writer")
    val writer: String
) : DTO, Parcelable
