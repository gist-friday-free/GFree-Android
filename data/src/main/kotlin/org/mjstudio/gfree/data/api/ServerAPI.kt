package org.mjstudio.gfree.data.api

import retrofit2.http.GET

interface ServerAPI {

    @GET("running/")
    suspend fun isServerRunning(): Boolean
}