package org.mjstudio.gfree.domain.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class UserInfoDTO(
    @PrimaryKey
    @SerializedName("uid")
     val uid: String,
    @SerializedName("email")
     val email: String,
    @SerializedName("majorCode")
     val majorCode: String,
    @SerializedName("studentId")
     val studentId: Int,
    @SerializedName("sex")
     val sex: Int,
    @SerializedName("age")
     val age: Int
) : DTO