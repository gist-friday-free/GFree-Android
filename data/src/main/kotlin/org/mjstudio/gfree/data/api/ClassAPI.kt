package org.mjstudio.gfree.data.api

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
    suspend fun createClassData(@Body body: ClassDataDTO): ClassDataDTO

    @GET("class/")
    suspend fun getClassDataWithCode(
            @Query("code") code : String,
            @Query("year") year : Int,
            @Query("semester") semester : Int
    ): ClassDataDTO

    @GET("class/")
    suspend fun getClassDataList(
        @Query("year") year: Int,
        @Query("semester") semester: Int
    ): List<ClassDataDTO>

    @GET("class/{id}/users")
    suspend fun getUsersInClass(@Path("id") id: Int): List<UserInfoDTO>
    @GET("class/{id}/reviews")
    suspend fun getReviewsInClass(@Path("id") id: Int): List<ReviewDTO>

    @GET("class/{id}/edits")
    suspend fun getEditsInClass(@Path("id") id: Int): List<EditDTO>

    @GET("class/{id}")
    suspend fun getClassData(@Path("id") id: Int): ClassDataDTO

    @PUT("class/{id}")
    suspend fun updateClassData(@Path("id") id: Int): ClassDataDTO

    @DELETE("class/{id}")
    suspend fun deleteClassData(@Path("id") id: Int): ClassDataDTO


}