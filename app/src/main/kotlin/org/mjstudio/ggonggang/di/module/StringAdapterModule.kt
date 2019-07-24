package org.mjstudio.ggonggang.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import org.mjstudio.gfree.domain.enumerator.Major
import org.mjstudio.gfree.domain.enumerator.Sex
import javax.inject.Singleton

@Module
class StringAdapterModule {

    @Provides
    @Singleton
    fun provideMajorStringAdapter(app : Application) : Major.StringAdapter {
        return Major.StringAdapter(app.resources)
    }

    @Provides
    @Singleton
    fun provideSexStringAdapter(app : Application) : Sex.StringAdapter {
        return Sex.StringAdapter(app.resources)
    }
}