package org.mjstudio.gfree.data.repository

import org.mjstudio.gfree.data.api.ClassAPI
import org.mjstudio.gfree.domain.adapter.toDTO
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.dto.ClassDataListResponse
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.entity.Edit
import org.mjstudio.gfree.domain.entity.UserInfo
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassDataRepositoryImpl @Inject constructor(private val classAPI: ClassAPI) : ClassDataRepository {


    override suspend fun getClassDataListWithPage(year: Int, semester: Int, page : Int): ClassDataListResponse {
        return classAPI.getClassDataListWithPage(year,semester,page)
    }

    override suspend fun getClassDataList(year: Int, semester: Int): ClassDataListResponse {
        return classAPI.getClassDataList(year, semester)
    }

    override suspend fun getClassData(id: Int): ClassData {
        return classAPI.getClassData(id).toEntity()
    }

    override suspend fun addClass(data: ClassData) : ClassData {
        return classAPI.createClassData(data.toDTO()).toEntity()
    }

    override suspend fun deleteClass(data: ClassData) : ClassData {
        return classAPI.deleteClassData(data.id).toEntity()
    }



    override suspend fun getParticipantCount(year: Int, semester: Int): Int {
        TODO("not implemented") // To change body of created suspend functions use File | Settings | File Templates.
    }

    override suspend fun getUsersInClass(id: Int): List<UserInfo> {
        return classAPI.getUsersInClass(id).toEntity().toList()
    }

    override suspend fun getReviewsInClass(id: Int): List<ReviewDTO>{
        return classAPI.getReviewsInClass(id)
    }

    override suspend fun getEditsInClass(id: Int): List<Edit> {
        return classAPI.getEditsInClass(id).toEntity().toList()
    }

    override suspend fun getClassDataWithCode(code: String, year: Int, semester: Int): ClassData {
        return classAPI.getClassDataWithCode(code,year,semester).toEntity()
    }
}