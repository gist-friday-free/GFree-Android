package org.mjstudio.gfree.data.api

import io.reactivex.Single
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
    fun createEditRequest(@Body body : EditDTO) : Single<EditDTO>

    @GET("edit/")
    fun getEditList(
        @Query("year") year: Int,
        @Query("semester") semester: Int
    ): Single<List<EditDTO>>

    @GET("edit/")
    fun getEditListWithCode(
            @Query("code") code : String,
            @Query("year") year : Int,
            @Query("semester") semester : Int
    ): Single<List<EditDTO>>

    @GET("edit/{id}/users")
    fun getHeartUsersInEdit(@Path("id")id : Int) : Single<List<UserInfoDTO>>

    @PUT("edit/{id}")
    fun updateStar(
            @Path("id") id : Int,
            @Query("add") add : Int,
            @Query("uid") uid : String
    ) : Single<EditDTO>

}