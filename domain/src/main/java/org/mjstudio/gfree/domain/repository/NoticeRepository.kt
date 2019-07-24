package org.mjstudio.gfree.domain.repository

import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.NoticeDTO

interface NoticeRepository {
    fun getNoticeList(page: Int): Single<List<NoticeDTO>>
}