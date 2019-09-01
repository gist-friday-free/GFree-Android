package org.mjstudio.ggonggang.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.mjstudio.ggonggang.util.NotificationHelper

class FirebaseMessageService : FirebaseMessagingService() {
    companion object {
        private val TAG = FirebaseMessageService::class.java.simpleName
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.from)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Message data payload: " + remoteMessage.data)
        }
        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
        }

        NotificationHelper.handleMessage(this, remoteMessage)
    }
}
