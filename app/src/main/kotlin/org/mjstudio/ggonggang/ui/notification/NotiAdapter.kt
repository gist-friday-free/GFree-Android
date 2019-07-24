package org.mjstudio.ggonggang.ui.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.mjstudio.gfree.domain.dto.NotiList
import org.mjstudio.ggonggang.BR
import org.mjstudio.ggonggang.databinding.ItemNotiBinding
import org.mjstudio.ggonggang.ui.notification.NotiAdapter.NotiViewHolder
import java.text.SimpleDateFormat
import java.util.*

class NotiAdapter : RecyclerView.Adapter<NotiViewHolder>() {

    var items : NotiList = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotiViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotiBinding.inflate(inflater,parent,false)

        return NotiViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: NotiViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class NotiViewHolder(private val binding: ItemNotiBinding) : ViewHolder(binding.root) {

        fun bind(item : Any) {
            binding.setVariable(BR.item,item)
            binding.executePendingBindings()
        }

    }
}
@BindingAdapter("app:notiItems")
fun RecyclerView.setNotiItems(items : NotiList) {
    (this.adapter as? NotiAdapter)?.items = items
    this.adapter?.notifyDataSetChanged()
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("app:notiDateTime")
fun TextView.setNotiDateTime(time : Long) {
    val calendar = Calendar.getInstance()
    calendar.time = Date(time)

    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    this.text = format.format(calendar.time)
}