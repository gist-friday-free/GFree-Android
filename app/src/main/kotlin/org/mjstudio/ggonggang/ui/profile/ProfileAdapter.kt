package org.mjstudio.ggonggang.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.ggonggang.BR
import org.mjstudio.ggonggang.databinding.ItemProfileClassBinding
import org.mjstudio.ggonggang.ui.profile.ProfileAdapter.ProfileViewHolder

class ProfileAdapter : RecyclerView.Adapter<ProfileViewHolder>() {

    var items : List<ClassData> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val binding = ItemProfileClassBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ProfileViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class ProfileViewHolder(private val binding : ItemProfileClassBinding) : ViewHolder(binding.root) {

        fun bind(item : Any) {
            binding.setVariable(BR.item,item)
            binding.executePendingBindings()
        }
    }
}