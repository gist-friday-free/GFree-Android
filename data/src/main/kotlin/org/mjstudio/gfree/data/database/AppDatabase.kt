package org.mjstudio.gfree.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.mjstudio.gfree.domain.dto.NotiDTO

@Database(entities=[NotiDTO::class],version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNotiDAO() : NotiDAO
}

