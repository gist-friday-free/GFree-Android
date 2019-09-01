package org.mjstudio.gfree.domain.dto

import com.google.gson.annotations.SerializedName

data class ClassDataListResponse(
        val count : Int,
        val previous : String?,
        val next : String?,
        @SerializedName("results")
        val classes : List<ClassDataDTO>
)