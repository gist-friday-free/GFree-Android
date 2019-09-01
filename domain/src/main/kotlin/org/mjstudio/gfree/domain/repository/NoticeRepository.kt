package org.mjstudio.gfree.domain.repository

import org.mjstudio.gfree.domain.dto.NoticeDTO

interface NoticeRepository {
    suspend fun getNoticeList(page: Int):List<NoticeDTO>
}