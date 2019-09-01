package org.mjstudio.gfree.data.repository

import org.mjstudio.gfree.data.api.NoticeAPI
import org.mjstudio.gfree.domain.dto.NoticeDTO
import org.mjstudio.gfree.domain.repository.NoticeRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoticeRepositoryImpl @Inject constructor(private val noticeAPI: NoticeAPI) : NoticeRepository {

    override suspend fun getNoticeList(page: Int): List<NoticeDTO> {
        return noticeAPI.getNoticeList(page)
    }
}