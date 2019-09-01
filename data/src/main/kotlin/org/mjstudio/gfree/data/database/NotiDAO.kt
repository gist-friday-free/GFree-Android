package org.mjstudio.gfree.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.NotiDTO

@Dao
interface NotiDAO {

    @Insert
    fun insertNoti(item : NotiDTO) : Completable

    @Insert
    fun insertAll(vararg items : NotiDTO) : Completable

    @Query("SELECT * FROM Notification ORDER BY created DESC LIMIT :limit")
    fun getRecentNotifications(limit : Int) : Single<List<NotiDTO>>
}