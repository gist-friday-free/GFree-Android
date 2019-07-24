package org.mjstudio.gfree.data.api

import io.reactivex.Single
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
    fun createUser(@Body body: UserInfoDTO): Single<UserInfoDTO>

    @GET("user/{uid}")
    fun getUser(@Path("uid") uid: String): Single<UserInfoDTO>

    @PUT("user/{uid}")
    fun updateUser(
            @Path("uid") uid: String,
            @Body body: UserInfoDTO,
            @Query("classData") classData: Int? = null,
            @Query("remove") remove: String? = null
    ): Single<UserInfoDTO>

    @DELETE("user/{uid}")
    fun deleteUser(@Path("uid") uid: String): Single<UserInfoDTO>

    @GET("user/{uid}/class")
    fun getClassesListWithUid(
        @Path("uid") uid: String,
        @Query("year") year: Int,
        @Query("semester") semester: Int
    ): Single<List<ClassDataDTO>>
}