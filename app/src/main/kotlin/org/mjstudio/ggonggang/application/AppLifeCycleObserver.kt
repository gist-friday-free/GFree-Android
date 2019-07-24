package org.mjstudio.ggonggang.application

import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.ggonggang.util.NotificationHelper

class AppLifeCycleObserver(private val mApplication: GFreeApplication) : LifecycleObserver {

    companion object {
        private val TAG = AppLifeCycleObserver::class.java.simpleName
    }

    /**
     * When app enters foreground
     */
    @OnLifecycleEvent(ON_START)
    fun onStart() {
        // 앱이 설치되고 처음 실행될 때
        if (!sp.loadBoolean(Constant.IS_NOT_FIRST_RUN)) {
            NotificationHelper.createChannels(context = mApplication)

           sp.saveBoolean(Constant.IS_NOT_FIRST_RUN, true)
        }
    }
}