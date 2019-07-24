package org.mjstudio.ggonggang.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.mjstudio.gfree.domain.constant.NativeConstant
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(converter: GsonConverterFactory, adapter: RxJava2CallAdapterFactory, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(adapter)
                .addConverterFactory(converter)
                .client(client)
                .baseUrl(NativeConstant.API_URL)
                .build()
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }
    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

//        return object :  Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//                val request = chain.request()
//                        .newBuilder()
//                        .addHeader("Authorization","Api-Key ${NativeConstant.getAPISerialKey()}")
//                        .build()
//                return chain.proceed(request)
//            }
//        }
    }

    @Provides
    @Singleton
    fun provideGsonConverter(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideRxJava2Adapter(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
//        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        return GsonBuilder().create()
    }
}