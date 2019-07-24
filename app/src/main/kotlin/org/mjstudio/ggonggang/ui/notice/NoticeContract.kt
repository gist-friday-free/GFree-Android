package org.mjstudio.ggonggang.ui.notice

import org.mjstudio.gfree.domain.dto.NoticeDTO
import org.mjstudio.ggonggang.base.BasePresenter
import org.mjstudio.ggonggang.base.BaseView

interface NoticeContract {
    interface View : BaseView {
        fun setNotice(items: List<NoticeDTO>)
    }
    interface Presenter : BasePresenter<View> {
        fun requestNotice()
    }
}