package org.mjstudio.gfree.domain.repository

import org.mjstudio.gfree.domain.dto.ClassDataListResponse
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.entity.Edit
import org.mjstudio.gfree.domain.entity.UserInfo

interface ClassDataRepository {
    suspend fun getClassDataListWithPage(year : Int, semester : Int,page : Int): ClassDataListResponse

    suspend fun getClassDataList(year: Int, semester: Int): ClassDataListResponse
    suspend fun getClassData(id: Int): ClassData

    suspend fun addClass(data: ClassData) : ClassData
    suspend fun deleteClass(data: ClassData) : ClassData


    suspend fun getParticipantCount(year: Int, semester: Int): Int
    suspend fun getUsersInClass(id: Int): List<UserInfo>
    suspend fun getReviewsInClass(id: Int): List<ReviewDTO>
    suspend fun getEditsInClass(id : Int): List<Edit>

    suspend fun getClassDataWithCode(code : String, year: Int, semester : Int) : ClassData
}