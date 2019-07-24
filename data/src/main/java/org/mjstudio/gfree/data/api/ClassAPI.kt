package org.mjstudio.gfree.data.api

import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.ClassDataDTO
import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ClassAPI {

    @POST("class/")
    fun createClassData(@Body body: ClassDataDTO): Single<ClassDataDTO>

    @GET("class/")
    fun getClassDataWithCode(
            @Query("code") code : String,
            @Query("year") year : Int,
            @Query("semester") semester : Int
    ): Single<ClassDataDTO>

    @GET("class/")
    fun getClassDataList(
        @Query("year") year: Int,
        @Query("semester") semester: Int
    ): Single<List<ClassDataDTO>>

    @GET("class/{id}/users")
    fun getUsersInClass(@Path("id") id: Int): Single<List<UserInfoDTO>>
    @GET("class/{id}/reviews")
    fun getReviewsInClass(@Path("id") id: Int): Single<List<ReviewDTO>>

    @GET("class/{id}/edits")
    fun getEditsInClass(@Path("id") id: Int): Single<List<EditDTO>>

    @GET("class/{id}")
    fun getClassData(@Path("id") id: Int): Single<ClassDataDTO>

    @PUT("class/{id}")
    fun updateClassData(@Path("id") id: Int): Single<ClassDataDTO>

    @DELETE("class/{id}")
    fun deleteClassData(@Path("id") id: Int): Single<ClassDataDTO>


}