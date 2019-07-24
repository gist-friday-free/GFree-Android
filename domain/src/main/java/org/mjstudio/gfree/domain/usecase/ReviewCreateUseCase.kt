// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.common.ResultState
// import org.mjstudio.gfree.domain.dto.ReviewDTO
// import org.mjstudio.gfree.domain.repository.ReviewRepository
// import org.mjstudio.gfree.domain.usecase.ReviewCreateUseCase.Params
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class ReviewCreateUseCase @Inject constructor(val repository: ReviewRepository)
//    : SingleUseCase<ResultState, Params>() {
//
//    override fun buildUseCaseSingle(params: Params): Single<ResultState> {
//        return repository.createReview(params.review)
//    }
//
//    data class Params(val review : ReviewDTO)
// }