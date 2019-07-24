package org.mjstudio.ggonggang.application

import android.content.res.Resources
import android.graphics.Point
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.PlayStoreDTO
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.ggonggang.di.AppComponent
import org.mjstudio.ggonggang.di.DaggerAppComponent
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

    @Inject
    lateinit var playStoreInfoSingle: Single<PlayStoreDTO>
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

        val appLifeCycleObserver = AppLifeCycleObserver(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifeCycleObserver)
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
