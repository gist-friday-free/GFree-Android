// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import org.mjstudio.gfree.domain.dto.NoticeDTO
// import org.mjstudio.gfree.domain.repository.NoticeRepository
// import org.mjstudio.gfree.domain.usecase.NoticeListUseCase.Params
// import javax.inject.Inject
// import javax.inject.Singleton
//
// @Singleton
// class NoticeListUseCase @Inject constructor(private val repository : NoticeRepository)
//    : SingleUseCase<List<NoticeDTO>, Params>()  {
//
//    override fun buildUseCaseSingle(params: Params): Single<List<NoticeDTO>> {
//        return with(params) {
//            repository.getNoticeList(page)
//        }
//
//    }
//
//    class Params(
//            val page : Int
//    )
// }