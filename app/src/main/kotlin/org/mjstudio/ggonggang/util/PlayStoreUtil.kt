package org.mjstudio.ggonggang.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity

object PlayStoreUtil {
    fun openPlayStore(context: Context) {
        val appPackageName = context.applicationContext.packageName
        try {

            val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            startActivity(context, intent, null)
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }
}