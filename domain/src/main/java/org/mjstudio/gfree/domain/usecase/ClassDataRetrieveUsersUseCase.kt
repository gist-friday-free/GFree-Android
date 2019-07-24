// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.dto.UserInfoDTO
// import org.mjstudio.gfree.domain.repository.ClassDataRepository
// import org.mjstudio.gfree.domain.usecase.ClassDataRetrieveUsersUseCase.Params
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class ClassDataRetrieveUsersUseCase @Inject constructor(private val repository : ClassDataRepository)
//    : SingleUseCase<List<UserInfoDTO>, Params>()  {
//
//
//    override fun buildUseCaseSingle(params : Params): Single<List<UserInfoDTO>> {
//        return with(params) {
//            repository.getUsersInClass(id)
//        }
//    }
//
//    class Params(
//            val id : Int
//    )
// }