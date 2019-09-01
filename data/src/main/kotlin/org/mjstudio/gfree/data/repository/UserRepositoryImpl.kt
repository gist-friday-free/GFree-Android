package org.mjstudio.gfree.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.rxkotlin.toSingle
import org.mjstudio.gfree.data.api.UserAPI
import org.mjstudio.gfree.domain.adapter.toDTO
import org.mjstudio.gfree.domain.adapter.toEntity
import org.mjstudio.gfree.domain.common.addSchedulers
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

    override fun createAccount(info: UserInfoDTO): Single<UserInfoDTO> {
        return Single.create { emitter ->
            userAPI.createUser(info)
                    .addSchedulers()
                    .subscribe({
                        emitter.onSuccess(it)
                    }, {
                        emitter.onError(it)
                    })
        }
    }

    override fun changeStudentId(uid: String, id: Int): Single<Int> {
        return Single.create { emitter ->
            getUserInfo(uid)
                    .addSchedulers()
                    .subscribe({ userInfo ->
                        userAPI.updateUser(uid, userInfo.copy(studentId = id))
                                .addSchedulers()
                                .subscribe({
                                    emitter.onSuccess(id)
                                }, {
                                    emitter.onError(it)
                                })
                    }, {

                        emitter.onError(it)
                    })
        }
    }

    override fun changeMajor(uid: String, majorCode: String): Single<String> {
        return Single.create { emitter ->
            getUserInfo(uid)
                    .addSchedulers()
                    .subscribe({ userInfo ->
                        userAPI.updateUser(uid, userInfo.copy(majorCode = majorCode))
                                .addSchedulers()
                                .subscribe({
                                    emitter.onSuccess(majorCode)
                                }, {
                                    emitter.onError(it)
                                })
                    }, {

                        emitter.onError(it)
                    })
        }
    }

    override fun changeAge(uid: String, age: Int): Single<Int> {
        return Single.create { emitter ->
            getUserInfo(uid)
                    .addSchedulers()
                    .subscribe({ userInfo ->
                        userAPI.updateUser(uid, userInfo.copy(age = age))
                                .addSchedulers()
                                .subscribe({
                                    emitter.onSuccess(age)
                                }, {
                                    emitter.onError(it)
                                })
                    }, {

                        emitter.onError(it)
                    })
        }
    }

    override fun changeSex(uid: String, sex: Int): Single<Int> {
        return Single.create { emitter ->
            getUserInfo(uid)
                    .addSchedulers()
                    .subscribe({ userInfo ->
                        userAPI.updateUser(uid, userInfo.copy(sex = sex))
                                .addSchedulers()
                                .subscribe({
                                    emitter.onSuccess(sex)
                                }, {
                                    emitter.onError(it)
                                })
                    }, {

                        emitter.onError(it)
                    })
        }
    }

    override fun getUserInfo(uid: String): Single<UserInfoDTO> {
        return userAPI.getUser(uid)
    }

    override fun getClassesWithUid(uid: String, year: Int, semester: Int): Single<List<ClassDataDTO>> {
        if(registeredClassesCache.value != null && isRegisteredClassesCacheLoaded)
            return registeredClassesCache.value!!.toDTO().toList().toSingle()
        else {
            return userAPI.getClassesListWithUid(uid, year, semester)
                    .doOnSuccess {
                        isRegisteredClassesCacheLoaded = true

                        synchronized(registeredClassesCache) {
                            registeredClassesCache.postValue(it.toEntity().toList())
                        }
                    }
        }
    }

    override fun registerClass(uid: String, classData: ClassData): Single<ClassData> {
        return Single.create { emitter ->
            getUserInfo(uid)
                    .addSchedulers()
                    .subscribe({ userInfo ->
                        userAPI.updateUser(uid, userInfo, classData.id, "false")
                                .addSchedulers()
                                .subscribe({
                                    emitter.onSuccess(classData)
                                }, {
                                    emitter.onError(it)
                                })
                    }, {

                        emitter.onError(it)
                    })
        }
    }

    override fun unregisterClass(uid: String, classData: ClassData): Single<ClassData> {
        return Single.create { emitter ->
            getUserInfo(uid)
                    .addSchedulers()
                    .subscribe({ userInfo ->
                        userAPI.updateUser(uid, userInfo, classData.id, "true")
                                .addSchedulers()
                                .subscribe({
                                    emitter.onSuccess(classData)
                                }, {
                                    emitter.onError(it)
                                })
                    }, {

                        emitter.onError(it)
                    })
        }
    }
}