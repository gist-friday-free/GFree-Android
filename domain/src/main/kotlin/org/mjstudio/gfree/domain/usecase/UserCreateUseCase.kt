// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.common.ResultState
// import org.mjstudio.gfree.domain.dto.UserInfoDTO
// import org.mjstudio.gfree.domain.entity.UserInfo
// import org.mjstudio.gfree.domain.repository.UserRepository
// import org.mjstudio.gfree.domain.usecase.UserCreateUseCase.Params
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class UserCreateUseCase @Inject constructor(val userRepository: UserRepository) : SingleUseCase<ResultState, Params>() {
//
//    override fun buildUseCaseSingle(params: Params): Single<ResultState> {
//        return with(params) {
//            userRepository.createAccount(
//                    UserInfoDTO(
//                            account.uid,
//                            account.email,
//                            account.major,
//                            account.id,
//                            account.sex,
//                            account.age
//                    )
//            )
//        }
//    }
//
//    data class Params(
//            val account : UserInfo
//    )
// }