package org.mjstudio.gfree.data.api

import org.mjstudio.gfree.domain.dto.PlayStoreDTO
import retrofit2.http.GET

interface PlayStoreAPI {

    @GET("playstore/")
    suspend fun getPlayStoreInfo(): PlayStoreDTO
}