package org.mjstudio.gfree.domain.repository

import org.mjstudio.gfree.domain.dto.ClassDataDTO
import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.ClassData

interface ClassDataRepository {
    suspend fun getClassDataList(year: Int, semester: Int): List<ClassDataDTO>
    suspend fun getClassData(id: Int): ClassDataDTO

    suspend fun addClass(data: ClassData)
    suspend fun deleteClass(data: ClassData)

    suspend fun getRegisteredClassDataList(year: Int, semester: Int): List<ClassDataDTO>


    suspend fun getParticipantCount(year: Int, semester: Int): Int
    suspend fun getUsersInClass(id: Int): List<UserInfoDTO>
    suspend fun getReviewsInClass(id: Int): List<ReviewDTO>
    suspend fun getEditsInClass(id : Int): List<EditDTO>

    suspend fun getClassDataWithCode(code : String, year: Int, semester : Int) : ClassDataDTO
}