package org.mjstudio.gfree.data.api

import org.mjstudio.gfree.domain.dto.ClassDataDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserAPI {

//    @FormUrlEncoded
    @POST("user/")
    suspend fun createUser(@Body body: UserInfoDTO): UserInfoDTO

    @GET("user/{uid}")
    suspend fun getUser(@Path("uid") uid: String): UserInfoDTO

    @PUT("user/{uid}")
    suspend fun updateUser(
            @Path("uid") uid: String,
            @Body body: UserInfoDTO,
            @Query("classData") classData: Int? = null,
            @Query("remove") remove: String? = null
    ): UserInfoDTO

    @DELETE("user/{uid}")
    suspend fun deleteUser(@Path("uid") uid: String): UserInfoDTO

    @GET("user/{uid}/class")
    suspend fun getClassesListWithUid(
        @Path("uid") uid: String,
        @Query("year") year: Int,
        @Query("semester") semester: Int
    ): List<ClassDataDTO>
}