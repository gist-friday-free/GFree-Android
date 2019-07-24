package org.mjstudio.ggonggang.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import org.mjstudio.ggonggang.ui.MainActivity

@Module
abstract class ActivityContributor {
    @MainActivityScope
    @ContributesAndroidInjector(modules = [
        FragmentContributor::class
    ])
    abstract fun contributeMainActivity() : MainActivity

}

