package org.mjstudio.ggonggang.di.module

import dagger.Module
import dagger.Provides
import org.mjstudio.gfree.data.api.ClassAPI
import org.mjstudio.gfree.data.api.EditAPI
import org.mjstudio.gfree.data.api.NoticeAPI
import org.mjstudio.gfree.data.api.PlayStoreAPI
import org.mjstudio.gfree.data.api.ReviewAPI
import org.mjstudio.gfree.data.api.ServerAPI
import org.mjstudio.gfree.data.api.UserAPI
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RetrofitAPIModule {
    @Provides
    @Singleton
    fun provideClassAPIModule(retrofit: Retrofit): ClassAPI {
        return retrofit.create(ClassAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUserAPI(retrofit: Retrofit): UserAPI {
        return retrofit.create(UserAPI::class.java)
    }

    @Provides
    @Singleton
    fun providePlayStoreAPI(retrofit: Retrofit): PlayStoreAPI {
        return retrofit.create(PlayStoreAPI::class.java)
    }
    @Provides
    @Singleton
    fun provideNoticeAPI(retrofit: Retrofit): NoticeAPI {
        return retrofit.create(NoticeAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideReviewAPI(retrofit: Retrofit): ReviewAPI {
        return retrofit.create(ReviewAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideServerAPI(retrofit: Retrofit): ServerAPI {
        return retrofit.create(ServerAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideEditAPI(retrofit : Retrofit): EditAPI {
        return retrofit.create(EditAPI::class.java)
    }
}