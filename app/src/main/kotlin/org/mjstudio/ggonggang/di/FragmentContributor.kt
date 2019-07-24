package org.mjstudio.ggonggang.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.ReviewRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import org.mjstudio.ggonggang.ui.MainFragment
import org.mjstudio.ggonggang.ui.auth.AuthFragment
import org.mjstudio.ggonggang.ui.auth.SignUpDialogFragment
import org.mjstudio.ggonggang.ui.edit.EditFragment
import org.mjstudio.ggonggang.ui.information.InformationFragment
import org.mjstudio.ggonggang.ui.information.InformationViewModelFactory
import org.mjstudio.ggonggang.ui.notification.NotiFragment
import org.mjstudio.ggonggang.ui.post.PostFragment
import org.mjstudio.ggonggang.ui.post.PostTypeSelectBottomSheet
import org.mjstudio.ggonggang.ui.post.PostViewModel
import org.mjstudio.ggonggang.ui.profile.ProfileFragment
import org.mjstudio.ggonggang.ui.search.SearchFilterBottomSheet
import org.mjstudio.ggonggang.ui.search.SearchFragment
import org.mjstudio.ggonggang.ui.splash.SplashFragment
import org.mjstudio.ggonggang.ui.timetable.TimeTableFragment

@Module
abstract class FragmentContributor {

    @SplashFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeSplashFragment() : SplashFragment

    @SignUpDialogFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeSignUpDialogFragment() : SignUpDialogFragment

    @AuthFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeAuthFragment() : AuthFragment

    @MainFragmentScope
    @ContributesAndroidInjector(modules = [MainFragmentSubComponentsContributor::class])
    abstract fun contributeMainFragment() : MainFragment

    @InformationFragmentScope
    @ContributesAndroidInjector(modules = [InformationFragmentModule::class])
    abstract fun contributeInformationFragment() : InformationFragment

    @SearchFilterDialogFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeSearchFilterDialogFragment() : SearchFilterBottomSheet

    @PostFragmentScope
    @ContributesAndroidInjector(modules= [PostFragmentSubComponentsContributor::class])
    abstract fun contributePostFragment() : PostFragment

}

@Module
abstract class PostFragmentSubComponentsContributor() {
    @Binds
    @PostFragmentScope
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    abstract fun bindPostViewModel(vm : PostViewModel) : ViewModel

//    @PostFragmentScope
    @ContributesAndroidInjector
    abstract fun contributePostEditTypeBottomSheet() : PostTypeSelectBottomSheet
}

@Module
class InformationFragmentModule() {
    @Provides
    fun provideInformationViewModelFactory(
            classDataRepository : ClassDataRepository,
            reviewRepository: ReviewRepository,
            userRepository: UserRepository,
            authRepository: FirebaseAuthRepository,
            infoFragment : InformationFragment
    ) : InformationViewModelFactory {

        val classData = infoFragment.classData
        return InformationViewModelFactory(classDataRepository,reviewRepository,userRepository,authRepository,classData)
    }
}

@Module
abstract class MainFragmentSubComponentsContributor {


    @TimeTableFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeTimeTableFragment() : TimeTableFragment

    @SearchFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeSearchFragment() : SearchFragment


    @EditFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeEditFragment() : EditFragment

    @NotiFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeNotiFragment() : NotiFragment

    @ProfileFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeProfileFragment() : ProfileFragment

}