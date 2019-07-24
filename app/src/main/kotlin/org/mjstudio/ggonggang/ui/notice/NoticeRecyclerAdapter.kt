package org.mjstudio.ggonggang.ui.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_notice.view.*
import org.mjstudio.gfree.domain.dto.NoticeDTO
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.toDateFormat

class NoticeRecyclerAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {

    private val clickSubject: PublishSubject<NoticeDTO> = PublishSubject.create()
    val clickEvent: Observable<NoticeDTO> = clickSubject

    var items: List<NoticeDTO> = listOf()
    var pos: Int = -1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return NoticeViewHolder(LayoutInflater.from(p0.context)
                .inflate(R.layout.item_notice, p0, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        (p0 as NoticeViewHolder).tv_title.text = items[p1].title
        p0.tv_writer.text = items[p1].writer

        p0.tv_time.text = items[p1].created.toDateFormat()
    }

    inner class NoticeViewHolder(mView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(mView) {
        init {
            mView.setOnClickListener {
                clickSubject.onNext(items[layoutPosition])
            }
        }
        val tv_title = mView.textView_notice_item_title
        val tv_writer = mView.textView_notice_item_writer
        val tv_time = mView.textView_notice_item_time
    }
}