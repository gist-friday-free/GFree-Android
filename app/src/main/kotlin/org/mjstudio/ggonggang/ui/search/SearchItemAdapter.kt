package org.mjstudio.ggonggang.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.application.appResources
import org.mjstudio.ggonggang.common.setIconRotation
import org.mjstudio.ggonggang.databinding.ItemClassBinding
import org.mjstudio.ggonggang.ui.search.SearchItemAdapter.SearchViewHolder
import org.mjstudio.ggonggang.util.ClassDataUtil

class SearchItemAdapter(private val userRepository: UserRepository, private val vm: SearchViewModel, private val viewLifecycleOwner : LifecycleOwner) : RecyclerView.Adapter<SearchViewHolder>() {

    var items: List<ClassData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val mBinding = ItemClassBinding.inflate(inflater, parent, false)
        mBinding.vm = vm

        return SearchViewHolder(mBinding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class SearchViewHolder(private val binding: ItemClassBinding) : RecyclerView.ViewHolder(binding.root) {

        private val TAG = SearchViewHolder::class.java.simpleName

        private var lastRegisteredClasses: List<ClassData> = listOf()
        private var needStoreRegisteredClasses = true

        init {


            /**
             * [MainViewModel]이 갖고 있는 [registeredClasses](현재 유저가 등록중인 과목들이 변경될 때마다,
             * [SearchFragment]의 RecyclerView의 아이템들의 Background 색이 변해야 한다.
             * [LifecycleOwner]객체는 SearchFragment로부터 [SearchItemAdapter]의 생성자로부터 전달받는다.
             */
            userRepository.getRegisteredClassDataLiveData().observe(viewLifecycleOwner, Observer{ registeredClasses ->
                lastRegisteredClasses = registeredClasses

                binding.item?.let {
                    checkDuplicateAndChangeBackground(registeredClasses, it)
                    needStoreRegisteredClasses=false
                }
            })
            /**
             * RecyclerView 아이템들의
             */
            vm.isMenuExpanded.observe(viewLifecycleOwner,Observer{expanded->
                if(!expanded)
                    binding.moreButton.setIconRotation(0)
                else if(vm.currentSelectedClassData.value?.name == binding.item?.name)
                    binding.moreButton.setIconRotation(90)
                else
                    binding.moreButton.setIconRotation(0)
            })
        }

        private fun checkDuplicateAndChangeBackground(registeredClasses : List<ClassData>,item: ClassData) {
            if (ClassDataUtil.checkDuplicate(registeredClasses, item).first) {
                binding.root.setBackgroundColor(appResources.getColor(R.color.colorTextHint))
            } else {
                binding.root.setBackgroundColor(appResources.getColor(R.color.colorBackground))
            }
        }

        fun bind(item: ClassData) {
            binding.item = item
            if(needStoreRegisteredClasses) {
                checkDuplicateAndChangeBackground(lastRegisteredClasses, item)
            }
            binding.executePendingBindings()
        }
    }
}
