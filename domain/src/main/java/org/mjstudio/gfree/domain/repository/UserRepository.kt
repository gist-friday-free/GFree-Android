package org.mjstudio.gfree.domain.repository

import androidx.lifecycle.LiveData
import io.reactivex.Single
import org.mjstudio.gfree.domain.dto.ClassDataDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.ClassData

interface UserRepository {

    fun createAccount(info: UserInfoDTO): Single<UserInfoDTO>

    fun changeStudentId(uid: String, id: Int): Single<Int>
    fun changeMajor(uid: String, majorCode: String): Single<String>
    fun changeAge(uid: String, age: Int): Single<Int>
    fun changeSex(uid: String, sex: Int): Single<Int>

    fun getUserInfo(uid: String): Single<UserInfoDTO>
    fun getClassesWithUid(uid: String, year: Int, semester: Int): Single<List<ClassDataDTO>>

    fun registerClass(uid: String, classData: ClassData): Single<ClassData>
    fun unregisterClass(uid: String, classData: ClassData): Single<ClassData>

//cache
    fun getRegisteredClassDataLiveData() : LiveData<List<ClassData>>
    fun addRegisteredClassDataToCache(classData : ClassData)
    fun removeRegisteredClasDataFromCache(classData : ClassData)

}