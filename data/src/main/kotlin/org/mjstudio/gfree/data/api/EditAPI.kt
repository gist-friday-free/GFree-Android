package org.mjstudio.gfree.data.api

import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EditAPI {

    @POST("edit/")
    suspend fun createEditRequest(@Body body : EditDTO) : EditDTO

    @GET("edit/")
    suspend fun getEditList(
        @Query("year") year: Int,
        @Query("semester") semester: Int
    ): List<EditDTO>

    @GET("edit/")
    suspend fun getEditListWithCode(
            @Query("code") code : String,
            @Query("year") year : Int,
            @Query("semester") semester : Int
    ): List<EditDTO>

    @GET("edit/{id}/users")
    suspend fun getHeartUsersInEdit(@Path("id")id : Int) : List<UserInfoDTO>

    @PUT("edit/{id}")
    suspend fun updateStar(
            @Path("id") id : Int,
            @Query("add") add : Int,
            @Query("uid") uid : String
    ) : EditDTO

}