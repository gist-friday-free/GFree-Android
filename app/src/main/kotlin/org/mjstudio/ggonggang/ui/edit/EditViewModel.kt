package org.mjstudio.ggonggang.ui.edit

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.item_edit.view.*
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.common.debugE
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.entity.Edit
import org.mjstudio.gfree.domain.repository.EditRepository
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.post
import javax.inject.Inject

class EditViewModel @Inject constructor(private val editRepository: EditRepository, private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    private val TAG = EditViewModel::class.java.simpleName

    //region DATA
    val isLoading = MutableLiveData(false)

    val compositeDisposable = CompositeDisposable()
    val editItems : MutableLiveData<List<Edit>> = MutableLiveData(listOf())

    val starClickedItemIds = MutableLiveData<Set<Int>>(setOf())
    //endregion


    //region EVENT
    val msg = MutableLiveData<Once<Msg>>()
    val snackMsg = MutableLiveData<Once<Msg>>()

    val clickPostButton = MutableLiveData<Once<Boolean>>()
    val startLayoutAnim = MutableLiveData<Once<Boolean>>()
    //endregion

    val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        getDatas()
    }

    init {
        getDatas()
    }


    private fun getDatas() {
        isLoading.value=true

        authRepository.getUid()?.let {uid->

            compositeDisposable += editRepository.getEditList(Constant.CURRENT_YEAR, Constant.CURRENT_SEMESTER)
                    .addSchedulers()
                    .doOnSubscribe { starClickedItemIds.value = setOf() }
                    .doAfterTerminate { isLoading.value = false }
                    .subscribe({
                        editItems.value = it.map {
                            val edit = it.toEntity()
                            if(edit.star.contains(uid))
                                starClickedItemIds.value = starClickedItemIds.value!!.plus(edit.id)
                            return@map edit
                        }
                        startLayoutAnim post Once(true)
                    }, {
                        msg.value = Once(NegativeMsg.EDIT_LIST_FAIL)
                        debugE(TAG, it)
                    })
        }
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }

    fun onClickPostButton() {
        clickPostButton.value = Once(true)
    }

    fun onClickHeartButton(v : View, item: Edit) {
        authRepository.getUid()?.let { uid ->
            if (item.id in starClickedItemIds.value!!) {
                compositeDisposable += editRepository.removeStar(item, uid).addSchedulers().subscribe({
                    starClickedItemIds.value = starClickedItemIds.value!! - it.id
                    val tv=(v.parent as ViewGroup).textView_star_count
                    tv.text = (tv.text.toString().toInt() -1).toString()
                }, {
                    debugE(TAG, it)
                })
            } else {
                compositeDisposable += editRepository.addStar(item, uid).addSchedulers().subscribe({
                    starClickedItemIds.value = starClickedItemIds.value!! + it.id
                    val tv=(v.parent as ViewGroup).textView_star_count
                    tv.text = (tv.text.toString().toInt() +1).toString()
                }, {
                    debugE(TAG, it)
                })
            }
        }
    }
}
@BindingAdapter("app:editItems")
fun RecyclerView.setEditItems(items : List<Edit>) {
    val adapter = this.adapter as? EditAdapter ?: return
    adapter.items = items
    adapter.notifyDataSetChanged()
}
@BindingAdapter("app:requested_text")
fun TextView.setRequestedEmailText(text : String) {
    //『』


    if(text.contains("『") && text.contains("』")){
        val spannable = SpannableString(text)
        val idx1= text.indexOf("『")
        val idx2 = text.indexOf("』")
        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.colorPrimary2)),idx1,idx2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        this.text = spannable
    }else {
        this.text = text
    }
}
