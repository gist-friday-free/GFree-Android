package org.mjstudio.gfree.domain.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReviewDTO(

    @SerializedName("id")
    @Expose(serialize = false,deserialize = true)
    var id: Int = -1,
    @SerializedName("reviewClass")
    @Expose
    var reviewClass: Int,
    @SerializedName("writer")
    @Expose
    var writer: String,
    @SerializedName("body")
    @Expose
    var body: String,
    @SerializedName("created")
    @Expose
    var created: Long

) : DTO