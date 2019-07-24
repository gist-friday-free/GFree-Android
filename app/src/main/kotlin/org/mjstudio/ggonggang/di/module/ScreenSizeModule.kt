package org.mjstudio.ggonggang.di.module

import android.app.Application
import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ScreenSizeModule {

    @Provides
    @Singleton
    fun provideScreenSize(context :Application) : Point {
        val wm= context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getSize(point)

        return point
    }
}