package org.mjstudio.gfree.data.api

import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.PlayStoreDTO
import retrofit2.http.GET

interface PlayStoreAPI {

    @GET("playstore/")
    fun getPlayStoreInfo(): Single<PlayStoreDTO>
}