package org.mjstudio.gfree.data.api

import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.NoticeDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NoticeAPI {

    @GET("notice/")
    fun getNoticeList(@Query("page") page: Int = 1): Single<List<NoticeDTO>>

    @GET("notice/{id}")
    fun getNotice(@Path("id") id: Int): Single<NoticeDTO>
}