package org.mjstudio.ggonggang.di.module

import dagger.Module
import dagger.Provides
import org.mjstudio.gfree.domain.datasource.ClassDataSource
import org.mjstudio.gfree.domain.datasource.ClassDataSourceFactory
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideClassDataSource(repo : ClassDataRepository) = ClassDataSource(repo)

    @Provides
    @Singleton
    fun provideClassDataSourceFactory(source : ClassDataSource) = ClassDataSourceFactory(source)
}