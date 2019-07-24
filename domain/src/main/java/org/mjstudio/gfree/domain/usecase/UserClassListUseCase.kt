// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.dto.ClassDataDTO
// import org.mjstudio.gfree.domain.repository.UserRepository
// import org.mjstudio.gfree.domain.usecase.UserClassListUseCase.Params
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class UserClassListUseCase @Inject constructor(private val repository : UserRepository)
//    : SingleUseCase<List<ClassDataDTO>, Params>()  {
//
//
//    override fun buildUseCaseSingle(params : Params): Single<List<ClassDataDTO>> {
//        return with(params) {
//            repository.getClassesWithUid(uid)
//        }
//    }
//
//    class Params(
//            val uid : Int
//    )
// }