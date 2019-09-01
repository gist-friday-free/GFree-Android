package org.mjstudio.gfree.data.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.mjstudio.gfree.data.database.AppDatabase
import org.mjstudio.gfree.data.database.NotiDAO
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(context : Application) : AppDatabase {
        return Room.databaseBuilder(context,AppDatabase::class.java,"APPDATABASE")
                .build()
    }

    @Provides
    @Singleton
    fun provideNotiDAO(database : AppDatabase) : NotiDAO {
        return database.getNotiDAO()
    }
}