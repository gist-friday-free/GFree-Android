package org.mjstudio.gfree.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import org.mjstudio.gfree.data.api.ClassAPI
import org.mjstudio.gfree.domain.adapter.ClassDataAdapter
import org.mjstudio.gfree.domain.common.addSchedulers
import org.mjstudio.gfree.domain.dto.ClassDataDTO
import org.mjstudio.gfree.domain.dto.EditDTO
import org.mjstudio.gfree.domain.dto.ReviewDTO
import org.mjstudio.gfree.domain.dto.UserInfoDTO
import org.mjstudio.gfree.domain.entity.ClassData
import org.mjstudio.gfree.domain.repository.ClassDataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassDataRepositoryImpl @Inject constructor(private val classAPI: ClassAPI) : ClassDataRepository {



    override fun getClassDataList(year: Int, semester: Int): Single<List<ClassDataDTO>> {
        return classAPI.getClassDataList(year, semester)
    }

    override fun getClassData(id: Int): Single<ClassDataDTO> {
        return classAPI.getClassData(id)
    }

    override fun addClass(data: ClassData): Completable {
        return Completable.create { emitter ->
            classAPI.createClassData(ClassDataAdapter.toStorage(data))
                    .addSchedulers()
                    .subscribe({
                        emitter.onComplete()
                    }, {
                        emitter.onError(it)
                    })
        }
    }

    override fun deleteClass(data: ClassData): Completable {
        return Completable.create { emitter ->
            classAPI.deleteClassData(data.id)
                    .addSchedulers()
                    .subscribe({
                        emitter.onComplete()
                    }, {
                        emitter.onError(it)
                    })
        }
    }


    override fun getRegisteredClassDataList(year: Int, semester: Int): Single<List<ClassDataDTO>> {
        return classAPI.getClassDataList(year, semester)
    }

    override fun getParticipantCount(year: Int, semester: Int): Single<Int> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getUsersInClass(id: Int): Single<List<UserInfoDTO>> {
        return classAPI.getUsersInClass(id)
    }

    override fun getReviewsInClass(id: Int): Single<List<ReviewDTO>> {
        return classAPI.getReviewsInClass(id)
    }

    override fun getEditsInClass(id: Int): Single<List<EditDTO>> {
        return classAPI.getEditsInClass(id)
    }

    override fun getClassDataWithCode(code: String, year: Int, semester: Int): Single<ClassDataDTO> {
        return classAPI.getClassDataWithCode(code,year,semester)
    }
}