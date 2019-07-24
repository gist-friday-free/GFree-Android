package org.mjstudio.gfree.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.ClassDataDTO
import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.ClassData

interface ClassDataRepository {
    fun getClassDataList(year: Int, semester: Int): Single<List<ClassDataDTO>>
    fun getClassData(id: Int): Single<ClassDataDTO>

    fun addClass(data: ClassData): Completable
    fun deleteClass(data: ClassData): Completable

    fun getRegisteredClassDataList(year: Int, semester: Int): Single<List<ClassDataDTO>>


    fun getParticipantCount(year: Int, semester: Int): Single<Int>
    fun getUsersInClass(id: Int): Single<List<UserInfoDTO>>
    fun getReviewsInClass(id: Int): Single<List<ReviewDTO>>
    fun getEditsInClass(id : Int): Single<List<EditDTO>>

    fun getClassDataWithCode(code : String, year: Int, semester : Int) : Single<ClassDataDTO>
}