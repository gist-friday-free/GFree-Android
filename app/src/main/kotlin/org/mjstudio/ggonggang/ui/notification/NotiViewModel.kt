package org.mjstudio.ggonggang.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import org.mjstudio.gfree.data.database.NotiDAO
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.dto.NotiDTO
import org.mjstudio.gfree.domain.dto.NotiList
import java.util.*
import javax.inject.Inject

class NotiViewModel @Inject constructor(private val dao : NotiDAO) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val msg  = MutableLiveData<Once<Msg>>()
    val snackMsg = MutableLiveData<Once<Msg>>()

    val notiItems : MutableLiveData<NotiList> = MutableLiveData(listOf(
            NotiDTO(-1,"title",Date().time,"body"),
            NotiDTO(-1,"title",Date().time,"body"),
            NotiDTO(-1,"title",Date().time,"body"),
            NotiDTO(-1,"title",Date().time,"body"),
            NotiDTO(-1,"title",Date().time,"body"),
            NotiDTO(-1,"title",Date().time,"body"),
            NotiDTO(-1,"title",Date().time,"body")
    ))

    val adapter = MutableLiveData(NotiAdapter())

    init {
        loadNotiDatas()
    }

    private fun loadNotiDatas() {
        compositeDisposable += dao.getRecentNotifications(10)
                .addSchedulers()
                .subscribe({
                    notiItems.value = it + (notiItems.value ?: listOf())
                }, {
                    msg.value = Once(NegativeMsg.NOTI_LIST_FAIL)
                })
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
