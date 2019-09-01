// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.dto.ClassDataDTO
// import org.mjstudio.gfree.domain.repository.ClassDataRepository
// import org.mjstudio.gfree.domain.usecase.ClassDataRetrieveUseCase.Params
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class ClassDataRetrieveUseCase @Inject constructor(private val repository : ClassDataRepository) : SingleUseCase<ClassDataDTO, Params>()  {
//
//
//    override fun buildUseCaseSingle(params : Params): Single<ClassDataDTO> {
//        return with(params) {
//            repository.getClassData(id)
//        }
//    }
//
//    class Params(
//            val id : Int
//    )
// }