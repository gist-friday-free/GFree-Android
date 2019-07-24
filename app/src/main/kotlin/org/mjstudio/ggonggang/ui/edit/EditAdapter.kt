package org.mjstudio.ggonggang.ui.edit

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.mjstudio.gfree.domain.entity.Edit
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.databinding.ItemEditBinding
import org.mjstudio.ggonggang.ui.edit.EditAdapter.EditViewHolder

class EditAdapter(private val viewModel : EditViewModel, private val viewLifecycleOwner : LifecycleOwner) : RecyclerView.Adapter<EditViewHolder>() {

    var items = listOf<Edit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEditBinding.inflate(inflater,parent,false)
        binding.vm = viewModel
        return EditViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: EditViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class EditViewHolder(val binding : ItemEditBinding) : ViewHolder(binding.root) {

        private val TAG = EditViewHolder::class.java.simpleName
        private var firstObserve : Boolean = true

        init {
            viewModel.starClickedItemIds.observe(viewLifecycleOwner, Observer {
                adjust(binding.item,it)
            })
        }

        fun bind(item : Edit) {
            binding.setVariable(org.mjstudio.ggonggang.BR.item,item)
            binding.executePendingBindings()

            if(firstObserve) {
                adjust(item, viewModel.starClickedItemIds.value!!)
            }
        }

        private fun adjust(myItem : Edit?, itemList : Set<Int>) {
            myItem ?: return
            firstObserve=false


            if(myItem.id in itemList) {
                binding.imageViewStar.setImageResource(R.drawable.ic_star_filled)
                val scaleX= ObjectAnimator.ofFloat(binding.imageViewStar,"scaleX",1f,1.5f).apply {
                    duration = 100L
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = 1
                    setAutoCancel(true)
                }
                val scaleY= ObjectAnimator.ofFloat(binding.imageViewStar,"scaleY",1f,1.5f).apply {
                    duration = 100L
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = 1
                    setAutoCancel(true)
                }
                AnimatorSet().apply {
                    playTogether(scaleX,scaleY)
                    start()
                }

            }else {
                binding.imageViewStar.setImageResource(R.drawable.ic_star_outline)
            }


        }
    }
}