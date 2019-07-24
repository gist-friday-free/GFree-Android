// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.common.ResultState
// import org.mjstudio.gfree.domain.entity.ClassData
// import org.mjstudio.gfree.domain.repository.ClassDataRepository
// import org.mjstudio.gfree.domain.usecase.ClassDataDeleteUseCase.Params
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class ClassDataDeleteUseCase @Inject constructor(private val repository : ClassDataRepository): SingleUseCase<ResultState,Params>() {
//
//
//    override fun buildUseCaseSingle(params: Params): Single<ResultState> {
//        return with(params) {
//            repository.deleteClass(classData)
//        }
//    }
//
//
//    data class Params(
//            val classData: ClassData
//    )
// }