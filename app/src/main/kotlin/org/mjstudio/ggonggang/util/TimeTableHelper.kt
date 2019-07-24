package org.mjstudio.ggonggang.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.ggonggang.R
import org.mjstudio.ggonggang.common.toast

object TimeTableHelper {

    fun isDuplicateClasses(subject1: ClassData, subject2: ClassData): Boolean {

        var isDuplicate = false

        outer@for (time1 in subject1.time) {
            for (time2 in subject2.time) {
                if (time1.isDuplicate(time2)) {
                    isDuplicate = true
                    break@outer
                }
            }
        }

        return isDuplicate
    }

    fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap? {
        try {
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.draw(c)

            return b
        } catch (ex: Exception) {
            return null
        }
    }

    fun takeScreenshot(activity: Activity): Bitmap? {

        try {
            val rootView = activity.getWindow().getDecorView().findViewById<LinearLayout>(R.id.container_timetable_capture)
            rootView.setDrawingCacheEnabled(true)
            return rootView.getDrawingCache()
        } catch (ex: Exception) {
            return null
        }
    }

    fun saveImage(activity: Activity, b: Bitmap, image_name: String) {

        // Save image to gallery
        val savedImageURL = MediaStore.Images.Media.insertImage(activity.getContentResolver(), b, image_name, "timetable capture")

        // Display saved image url to TextView
        activity.toast("Image saved to gallery.\n$savedImageURL")
    }
}