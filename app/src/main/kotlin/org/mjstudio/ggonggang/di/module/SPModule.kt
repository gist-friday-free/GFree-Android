package org.mjstudio.ggonggang.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import org.mjstudio.ggonggang.util.SPService
import javax.inject.Singleton

@Module
class SPModule {
    @Provides
    @Singleton
    fun provideSPService(context: Application): SPService {
        return SPService(context)
    }
}