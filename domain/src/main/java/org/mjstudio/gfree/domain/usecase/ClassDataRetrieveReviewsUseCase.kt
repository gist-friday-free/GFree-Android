// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.dto.ReviewDTO
// import org.mjstudio.gfree.domain.repository.ClassDataRepository
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class ClassDataRetrieveReviewsUseCase @Inject constructor(private val repository : ClassDataRepository)
//    : SingleUseCase<List<ReviewDTO>, ClassDataRetrieveReviewsUseCase.Params>()  {
//
//
//    override fun buildUseCaseSingle(params : ClassDataRetrieveReviewsUseCase.Params): Single<List<ReviewDTO>> {
//        return with(params) {
//            repository.getReviewsInClass(id)
//        }
//    }
//
//    class Params(
//            val id : Int
//    )
// }