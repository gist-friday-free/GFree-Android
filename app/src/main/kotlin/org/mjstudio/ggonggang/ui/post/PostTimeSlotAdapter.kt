package org.mjstudio.ggonggang.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.mjstudio.gfree.domain.entity.TimeSlot
import org.mjstudio.ggonggang.BR
import org.mjstudio.ggonggang.common.ItemTouchHelperAdapter
import org.mjstudio.ggonggang.databinding.ItemPostTimeslotAddBinding
import org.mjstudio.ggonggang.databinding.ItemPostTimeslotBinding
import org.mjstudio.ggonggang.ui.post.PostTimeSlotAdapter.PostTimeSlotViewHolder

class PostTimeSlotAdapter() : RecyclerView.Adapter<PostTimeSlotViewHolder>(), ItemTouchHelperAdapter {
    companion object {
        private const val TYPE_ADD = 0
        private const val TYPE_GENERAL = 1
    }

    var items : List<TimeSlot> = listOf()
    var needAddHeader = false


    override fun onItemDismiss(position: Int) {
        items = items.minus(items[position])
        this.notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostTimeSlotViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if(viewType == TYPE_ADD) {
            val binding = ItemPostTimeslotAddBinding.inflate(inflater,parent,false)

            return PostTimeSlotViewHolder(binding)
        }else {
            val binding = ItemPostTimeslotBinding.inflate(inflater,parent,false)

            return PostTimeSlotViewHolder(binding)
        }

    }

    override fun getItemCount() = if(needAddHeader) items.size + 1 else items.size

    override fun onBindViewHolder(holder: PostTimeSlotViewHolder, position: Int) {
        if(needAddHeader) {
            if(position == 0) return
            holder.bind(items[position-1])
        }else {
            holder.bind(items[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(needAddHeader && position == 0) TYPE_ADD
        else TYPE_GENERAL
    }

    inner class PostTimeSlotViewHolder(private val binding : ViewDataBinding) : ViewHolder(binding.root) {

        fun bind(item : TimeSlot) {
            binding.setVariable(BR.item,item)
            binding.executePendingBindings()
        }
    }
}