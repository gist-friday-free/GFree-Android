package org.mjstudio.gfree.data.repository

import io.reactivex.Single
import org.mjstudio.gfree.data.api.NoticeAPI
import org.mjstudio.gfree.domain.dto.NoticeDTO
import org.mjstudio.gfree.domain.repository.NoticeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoticeRepositoryImpl @Inject constructor(private val noticeAPI: NoticeAPI) : NoticeRepository {

    override fun getNoticeList(page: Int): Single<List<NoticeDTO>> {
        return noticeAPI.getNoticeList(page)
    }
}