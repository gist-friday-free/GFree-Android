package org.mjstudio.gfree.data.api

import org.mjstudio.gfree.domain.dto.ReviewDTO
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ReviewAPI {
    @GET("review/")
    suspend fun getReviewList(): List<ReviewDTO>

    @FormUrlEncoded
    @POST("review/")
    suspend fun createReview(@Body body: ReviewDTO): ReviewDTO
}