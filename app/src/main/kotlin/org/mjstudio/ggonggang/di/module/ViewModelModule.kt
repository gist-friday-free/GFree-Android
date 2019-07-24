package org.mjstudio.ggonggang.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import org.mjstudio.ggonggang.di.ViewModelFactory
import org.mjstudio.ggonggang.di.ViewModelKey
import org.mjstudio.ggonggang.ui.MainViewModel
import org.mjstudio.ggonggang.ui.auth.AuthViewModel
import org.mjstudio.ggonggang.ui.edit.EditViewModel
import org.mjstudio.ggonggang.ui.notification.NotiViewModel
import org.mjstudio.ggonggang.ui.profile.ProfileViewModel
import org.mjstudio.ggonggang.ui.search.SearchViewModel
import org.mjstudio.ggonggang.ui.timetable.TimeTableViewModel
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(vm: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TimeTableViewModel::class)
    abstract fun bindTimeTableViewModel(vm: TimeTableViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(vm: SearchViewModel): ViewModel



    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(vm: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(vm : ProfileViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NotiViewModel::class)
    abstract fun bindNotiViewModel(vm : NotiViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditViewModel::class)
    abstract fun bindEditViewModel(vm : EditViewModel) : ViewModel


}