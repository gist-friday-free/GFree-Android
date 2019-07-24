package org.mjstudio.ggonggang.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import org.mjstudio.gfree.data.di.module.DatabaseModule
import org.mjstudio.ggonggang.application.GFreeApplication
import org.mjstudio.ggonggang.di.module.FirebaseAuthModule
import org.mjstudio.ggonggang.di.module.RepositoryModule
import org.mjstudio.ggonggang.di.module.RetrofitAPIModule
import org.mjstudio.ggonggang.di.module.RetrofitModule
import org.mjstudio.ggonggang.di.module.SPModule
import org.mjstudio.ggonggang.di.module.ScreenSizeModule
import org.mjstudio.ggonggang.di.module.StringAdapterModule
import org.mjstudio.ggonggang.di.module.ViewModelModule
import javax.inject.Singleton

@Component(modules = [
    AndroidSupportInjectionModule::class,
    ActivityContributor::class,

    SPModule::class,
    RetrofitModule::class,
    FirebaseAuthModule::class,

    RetrofitAPIModule::class,
    RepositoryModule::class,

    ViewModelModule::class,
    ScreenSizeModule::class,
    DatabaseModule::class,

    StringAdapterModule::class
    ]
)
@Singleton
interface AppComponent : AndroidInjector<GFreeApplication> {

    override fun inject(gFreeApplication: GFreeApplication)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun app(app: Application): Builder
    }
}
