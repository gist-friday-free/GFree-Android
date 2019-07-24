package org.mjstudio.ggonggang.ui.information

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.ggonggang.BR
import org.mjstudio.ggonggang.databinding.ItemReviewBinding
import org.mjstudio.ggonggang.ui.information.InformationAdapter.InformationViewHolder

class InformationAdapter(private val mViewModel : InformationViewModel) : androidx.recyclerview.widget.RecyclerView.Adapter<InformationViewHolder>() {
    var items: List<ReviewDTO> = listOf()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InformationViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(p0.context),p0,false)
        binding.vm = mViewModel

        return InformationViewHolder(binding)
    }

    override fun getItemCount()=items.size


    override fun onBindViewHolder(p0: InformationViewHolder, p1: Int) {
        val item = items[p1]

        p0.bind(item)
    }

    inner class InformationViewHolder(private val binding : ViewDataBinding) : ViewHolder(binding.root) {
        fun bind(item : Any) {
            binding.setVariable(BR.item,item)
        }
    }
}