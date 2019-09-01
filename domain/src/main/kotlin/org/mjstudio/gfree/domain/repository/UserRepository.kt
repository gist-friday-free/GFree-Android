package org.mjstudio.gfree.domain.repository

import androidx.lifecycle.LiveData
import org.mjstudio.gfree.domain.dto.ClassDataDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.ClassData

interface UserRepository {

    suspend fun createAccount(info: UserInfoDTO): UserInfoDTO

    suspend fun changeStudentId(uid: String, id: Int): Int
    suspend fun changeMajor(uid: String, majorCode: String): String
    suspend fun changeAge(uid: String, age: Int): Int
    suspend fun changeSex(uid: String, sex: Int): Int

    suspend fun getUserInfo(uid: String): UserInfoDTO
    suspend fun getClassesWithUid(uid: String, year: Int, semester: Int): List<ClassDataDTO>

    suspend fun registerClass(uid: String, classData: ClassData): ClassData
    suspend fun unregisterClass(uid: String, classData: ClassData): ClassData

    //cache
    fun getRegisteredClassDataLiveData(): LiveData<List<ClassData>>
    fun addRegisteredClassDataToCache(classData: ClassData)
    fun removeRegisteredClasDataFromCache(classData: ClassData)

}