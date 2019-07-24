// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.dto.ClassDataDTO
// import org.mjstudio.gfree.domain.repository.ClassDataRepository
// import org.mjstudio.gfree.domain.usecase.ClassDataListUseCase.Params
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class ClassDataListUseCase @Inject constructor(private val repository : ClassDataRepository) : SingleUseCase<List<ClassDataDTO>, Params>()  {
//
//    override fun buildUseCaseSingle(params: Params): Single<List<ClassDataDTO>> {
//        return with(params) {
//            repository.getClassDataList(year,semester)
//        }
//
//    }
//
//    class Params(
//            val year: Int,
//            val semester : Int
//    )
// }