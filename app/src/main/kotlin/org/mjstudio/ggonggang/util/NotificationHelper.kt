package org.mjstudio.ggonggang.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.ui.MainActivity
import org.mjstudio.ggonggang.util.NotificationChannelEnum.GENERAL

enum class NotificationChannelEnum(val id: String) {
    GENERAL("GENERAL")
}
enum class NotificationTopic(val topic: String) {
    NOTICE("NOTICE")
}

object NotificationHelper {

    private val TAG = NotificationHelper::class.java.simpleName

    fun createChannels(context: Context) {

        val notiManager = NotificationManagerCompat.from(context)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val generalName = context.resources.getString(R.string.noti_general_name)
            val generalChannel = NotificationChannel(GENERAL.id, generalName, NotificationManager.IMPORTANCE_MAX).apply {
                enableVibration(true)
            }

            notiManager.createNotificationChannel(generalChannel)
        }

        FirebaseMessaging.getInstance().subscribeToTopic(NotificationTopic.NOTICE.topic)
                .addOnCompleteListener {
                    Log.e(TAG, "Subscribe NOTIFICATION")
                }
    }

    fun handleMessage(context: Context, remoteMessage: RemoteMessage) {
        val notiManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId: String = remoteMessage.data["channel"] ?: GENERAL.id

        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        var notification = NotificationCompat.Builder(context, channelId)
                .setContentTitle(remoteMessage.data["title"])
                .setContentText(remoteMessage.data["body"])
                .setSmallIcon(R.drawable.logo_256)
                .setVisibility(VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(PRIORITY_MAX)
                .setNumber(1)
                .setVibrate(arrayOf<Long>(100, 100, 100, 100).toLongArray())
                .build()

        notiManager.notify(-1, notification)
    }
}