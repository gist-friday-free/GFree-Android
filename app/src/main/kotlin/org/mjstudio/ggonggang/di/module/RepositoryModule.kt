package org.mjstudio.ggonggang.di.module

import dagger.Binds
import dagger.Module
import org.mjstudio.gfree.data.repository.ClassDataRepositoryImpl
import org.mjstudio.gfree.data.repository.EditRepositoryImpl
import org.mjstudio.gfree.data.repository.FirebaseAuthRepositoryImpl
import org.mjstudio.gfree.data.repository.NoticeRepositoryImpl
import org.mjstudio.gfree.data.repository.PlayStoreRepositoryImpl
import org.mjstudio.gfree.data.repository.ReviewRepositoryImpl
import org.mjstudio.gfree.data.repository.ServerRepositoryImpl
import org.mjstudio.gfree.data.repository.UserRepositoryImpl
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import org.mjstudio.gfree.domain.repository.EditRepository
import org.mjstudio.gfree.domain.repository.FirebaseAuthRepository
import org.mjstudio.gfree.domain.repository.NoticeRepository
import org.mjstudio.gfree.domain.repository.PlayStoreRepository
import org.mjstudio.gfree.domain.repository.ReviewRepository
import org.mjstudio.gfree.domain.repository.ServerRepository
import org.mjstudio.gfree.domain.repository.UserRepository
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideUserRepository(repo: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun provideClassDataRepository(repo: ClassDataRepositoryImpl): ClassDataRepository

    @Binds
    @Singleton
    abstract fun provideFirebaseAuthRepository(repo: FirebaseAuthRepositoryImpl): FirebaseAuthRepository

    @Binds
    @Singleton
    abstract fun providePlayStoreRepository(repo: PlayStoreRepositoryImpl): PlayStoreRepository

    @Binds
    @Singleton
    abstract fun provideReviewRepository(repo: ReviewRepositoryImpl): ReviewRepository

    @Binds
    @Singleton
    abstract fun provideNoticeRepository(repo: NoticeRepositoryImpl): NoticeRepository

    @Binds
    @Singleton
    abstract fun provideServerRepository(repo: ServerRepositoryImpl): ServerRepository

    @Binds
    @Singleton
    abstract fun provideEditRepository(repo : EditRepositoryImpl) : EditRepository
}