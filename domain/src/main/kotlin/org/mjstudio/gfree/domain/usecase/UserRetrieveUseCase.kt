// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.dto.UserInfoDTO
// import org.mjstudio.gfree.domain.repository.UserRepository
// import org.mjstudio.gfree.domain.usecase.UserRetrieveUseCase.Params
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class UserRetrieveUseCase @Inject constructor(private val repository : UserRepository)
//    : SingleUseCase<UserInfoDTO, Params>()  {
//
//
//    override fun buildUseCaseSingle(params : Params): Single<UserInfoDTO> {
//        return with(params) {
//            repository.getUserInfo(uid)
//        }
//    }
//
//    class Params(
//            val uid : Int
//    )
// }