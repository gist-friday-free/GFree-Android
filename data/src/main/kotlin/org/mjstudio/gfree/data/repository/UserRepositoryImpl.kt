package org.mjstudio.gfree.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.mjstudio.gfree.data.api.UserAPI
import org.mjstudio.gfree.domain.adapter.toDTO
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.dto.ClassDataDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val userAPI: UserAPI) : UserRepository {

    //region Registered ClassData Cache(Memory during running Application)
    /**
     * 현재 유저가 해당 년도, 해당 학기에 등록하고 있는 과목들
     */
    private var registeredClassesCache : MutableLiveData<List<ClassData>> = MutableLiveData(listOf())
    private var isRegisteredClassesCacheLoaded = false

    override fun getRegisteredClassDataLiveData(): LiveData<List<ClassData>> {

        synchronized(registeredClassesCache) {
            return registeredClassesCache
        }
    }

    override fun addRegisteredClassDataToCache(classData: ClassData) {
        synchronized(registeredClassesCache) {
            registeredClassesCache.value = registeredClassesCache.value!! + classData
        }
    }

    override fun removeRegisteredClasDataFromCache(classData: ClassData) {
        synchronized(registeredClassesCache) {
            registeredClassesCache.value = registeredClassesCache.value!! - classData
        }
    }

    //endregion

    override suspend fun createAccount(info: UserInfoDTO): UserInfoDTO {
        return userAPI.createUser(info)
    }

    override suspend fun changeStudentId(uid: String, id: Int): Int {
        val info = getUserInfo(uid)
        userAPI.updateUser(uid,info.copy(studentId = id))
        return id
    }

    override suspend fun changeMajor(uid: String, majorCode: String): String {
        val info = getUserInfo(uid)
        userAPI.updateUser(uid,info.copy(majorCode = majorCode))
        return majorCode
    }

    override suspend fun changeAge(uid: String, age: Int): Int {
        val info = getUserInfo(uid)
        userAPI.updateUser(uid,info.copy(age=age))
        return age
    }

    override suspend fun changeSex(uid: String, sex: Int): Int {
        val info = getUserInfo(uid)
        userAPI.updateUser(uid,info.copy(sex = sex))
        return sex
    }

    override suspend fun getUserInfo(uid: String): UserInfoDTO {
        return userAPI.getUser(uid)
    }

    override suspend fun getClassesWithUid(uid: String, year: Int, semester: Int): List<ClassDataDTO> {


        return if(registeredClassesCache.value != null && isRegisteredClassesCacheLoaded)
            registeredClassesCache.value!!.toDTO().toList()
        else {
            val list = userAPI.getClassesListWithUid(uid,year,semester)
            isRegisteredClassesCacheLoaded = true
            synchronized(registeredClassesCache) {
                registeredClassesCache.value = list.toEntity().toList()
            }
            list
        }
    }

    override suspend fun registerClass(uid: String, classData: ClassData): ClassData {
        val info = getUserInfo(uid)
        userAPI.updateUser(uid,info,classData.id,"false")
        return classData
    }

    override suspend fun unregisterClass(uid: String, classData: ClassData): ClassData {
        val info = getUserInfo(uid)
        userAPI.updateUser(uid,info,classData.id,"true")
        return classData
    }
}