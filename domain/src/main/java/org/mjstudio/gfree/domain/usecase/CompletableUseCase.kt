// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Completable
// import io.reactivex.android.schedulers.AndroidSchedulers
// import io.reactivex.schedulers.Schedulers
//
// abstract class CompletableUseCase<in Params> : UseCase() {
//
//    internal abstract fun buildUseCaseCompletable(param: Params): Completable
//
//    fun execute(
//            onComplete: () -> Unit,
//            onError:(e : Throwable) -> Unit,
//            onFinished: () -> Unit = {},
//            params: Params
//    ) {
//        disposeLast()
//        lastDisposable = buildUseCaseCompletable(params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doAfterTerminate(onFinished)
//
//                .subscribe(onComplete,onError)
//        lastDisposable?.let{compositeDisposable.add(it)}
//    }
// }