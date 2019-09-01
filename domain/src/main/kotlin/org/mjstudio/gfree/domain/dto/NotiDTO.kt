package org.mjstudio.gfree.domain.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notification")
data class NotiDTO(
       @PrimaryKey(autoGenerate = true)
       val id : Int = -1,

       val title : String,
       @ColumnInfo(name = "created",index = true)
       val created : Long,

       val body : String
)

typealias NotiList = List<NotiDTO>