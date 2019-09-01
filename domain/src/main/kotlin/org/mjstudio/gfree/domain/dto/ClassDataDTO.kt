package org.mjstudio.gfree.domain.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class ClassDataDTO(
    @PrimaryKey
    @SerializedName("id")
     val id: Int,
    @SerializedName("year")
     val year: Int,
    @SerializedName("semester")
     val semester: Int,
    @SerializedName("name")
     val name: String,
    @SerializedName("code")
     val code: String,
    @SerializedName("professor")
     val professor: String?,
    @SerializedName("place")
     val place: String?,
    @SerializedName("size")
     val size: Int?,
    @SerializedName("grade")
     val grade: Int?,

    val start1: Int?,
    val end1: Int?,
    val week1: String?,

    val start2: Int?,
    val end2: Int?,
    val week2: String?,

    val start3: Int?,
    val end3: Int?,
    val week3: String?,

    val start4: Int?,
    val end4: Int?,
    val week4: String?,

    val start5: Int?,
    val end5: Int?,
    val week5: String?
) : DTO