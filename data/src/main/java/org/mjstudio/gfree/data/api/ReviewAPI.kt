package org.mjstudio.gfree.data.api

import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.ReviewDTO
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ReviewAPI {
    @GET("review/")
    fun getReviewList(): Single<List<ReviewDTO>>

    @FormUrlEncoded
    @POST("review/")
    fun createReview(@Body body: ReviewDTO): Single<ReviewDTO>
}