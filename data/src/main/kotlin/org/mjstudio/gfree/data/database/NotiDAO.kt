package org.mjstudio.gfree.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.mjstudio.gfree.domain.dto.NotiDTO

@Dao
interface NotiDAO {

    @Insert
    suspend fun insertNoti(item : NotiDTO)

    @Insert
    suspend fun insertAll(vararg items : NotiDTO)

    @Query("SELECT * FROM Notification ORDER BY created DESC LIMIT :limit")
    suspend fun getRecentNotifications(limit : Int) : List<NotiDTO>
}