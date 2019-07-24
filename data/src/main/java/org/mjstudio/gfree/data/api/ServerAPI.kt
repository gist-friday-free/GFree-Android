package org.mjstudio.gfree.data.api

import io.reactivex.Single
import retrofit2.http.GET

interface ServerAPI {

    @GET("running/")
    fun isServerRunning(): Single<Boolean>
}