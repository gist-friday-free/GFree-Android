package org.mjstudio.ggonggang.application

import android.content.res.Resources
import android.graphics.Point
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import org.mjstudio.gfree.domain.constant.Constant
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.ggonggang.di.AppComponent
import org.mjstudio.ggonggang.di.DaggerAppComponent
import org.mjstudio.ggonggang.util.NotificationHelper
import org.mjstudio.ggonggang.util.SPService
import javax.inject.Inject

class GFreeApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent.builder().app(this).build()
        appComponent.inject(this)
        return appComponent
    }

    //region COMPONENT
    lateinit var appComponent: AppComponent
    //endregion

    @Inject
    lateinit var screenSize : Point

    @Inject
    lateinit var sp: SPService

    lateinit var appResources: Resources
    @Inject
    lateinit var authRepository: FirebaseAuthRepository


    var playStoreVersionName: String? = null
    var playStoreVersionCode: Int? = null

    companion object {
        lateinit var instance: GFreeApplication
    }

    override fun onCreate() {
        super.onCreate()
        GFreeApplication.instance = this
        appResources = resources


        // 앱이 설치되고 처음 실행될 때
        if (!sp.loadBoolean(Constant.IS_NOT_FIRST_RUN)) {
            firstRun()
            org.mjstudio.ggonggang.application.sp.saveBoolean(Constant.IS_NOT_FIRST_RUN, true)
        }
    }

    private fun firstRun() {
        // 앱이 설치되고 처음 실행될 때
        NotificationHelper.createChannels(context = this)
    }
}


val sp: SPService
get() = GFreeApplication.instance.sp

val appResources: Resources
get() = GFreeApplication.instance.appResources

val screenWidth: Int
get() = GFreeApplication.instance.screenSize.x
val screenHeight: Int
    get() = GFreeApplication.instance.screenSize.y
