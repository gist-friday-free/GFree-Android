package org.mjstudio.ggonggang.ui.notice

import io.reactivex.disposables.CompositeDisposable
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.repository.NoticeRepository
import org.mjstudio.ggonggang.ui.notice.NoticeContract.View

class NoticePresenter(private val noticeRepository: NoticeRepository) : NoticeContract.Presenter {

    override var view: View? = null
    override var compositeDisposable: CompositeDisposable? = null

    override fun attachView(v: View) {
        super.attachView(v)

        requestNotice()
    }

    override fun detachView() {
        super.detachView()
    }

    override fun requestNotice() {
        val d = noticeRepository
                .getNoticeList(1)
                .addSchedulers()
                .subscribe({
                    view?.setNotice(it)
                }, {
                })
        compositeDisposable?.add(d)
    }
}