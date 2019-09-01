package org.mjstudio.gfree.data.api

import org.mjstudio.gfree.domain.dto.NoticeDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NoticeAPI {

    @GET("notice/")
    suspend fun getNoticeList(@Query("page") page: Int = 1): List<NoticeDTO>

    @GET("notice/{id}")
    suspend fun getNotice(@Path("id") id: Int): NoticeDTO
}