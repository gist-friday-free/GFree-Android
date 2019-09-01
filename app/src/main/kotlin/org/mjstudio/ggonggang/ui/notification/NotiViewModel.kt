package org.mjstudio.ggonggang.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mjstudio.gfree.data.database.NotiDAO
import org.mjstudio.gfree.domain.common.Msg
import org.mjstudio.gfree.domain.common.NegativeMsg
import org.mjstudio.gfree.domain.common.Once
import org.mjstudio.gfree.domain.dto.NotiDTO
import org.mjstudio.gfree.domain.dto.NotiList
import java.util.*
import javax.inject.Inject

class NotiViewModel @Inject constructor(private val dao : NotiDAO) : ViewModel() {


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


    init {
        loadNotiDatas()
    }

    private fun loadNotiDatas() = viewModelScope.launch {
        try {
            val it = dao.getRecentNotifications(10)
            notiItems.value = it + (notiItems.value ?: listOf())
        }catch(e : Throwable) {
            msg.value = Once(NegativeMsg.NOTI_LIST_FAIL)
        }
    }

}
