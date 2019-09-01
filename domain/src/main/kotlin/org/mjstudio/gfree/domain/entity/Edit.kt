package org.mjstudio.gfree.domain.entity

import org.mjstudio.gfree.domain.dto.EditDTO
import java.util.*

@Suppress("DEPRECATION")
data class Edit(
        var id: Int = -1,
        var editClass: ClassData,
        var year: Int,
        var semester: Int,
        var writer: String,
        var created: Long,
        var type : EditDTO.Type,
        var value : String,
        var star : List<String>
) : Entity {

    fun getDateTimeString() : String {
        val date = Date(created*1000)
        val calendar = Calendar.getInstance()
        calendar.time = date

        return "${calendar.get(Calendar.YEAR)}. ${calendar.get(Calendar.MONTH) + 1}. ${calendar.get(Calendar.DATE)}."
    }

    constructor(classData : ClassData, userInfo : UserInfo, type : EditDTO.Type,value : String)
    :this(-1,classData,classData.year,classData.semester,userInfo.email,Date().time,type,value,listOf())

}