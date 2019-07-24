// package org.mjstudio.gfree.domain.usecase
//
// import io.reactivex.Single
// import io.reactivex.android.schedulers.AndroidSchedulers
// import io.reactivex.schedulers.Schedulers
//
// abstract class SingleUseCase<T, in Params> : UseCase() {
//
//    internal abstract fun buildUseCaseSingle(params: Params): Single<T>
//
//    fun execute(
//            onSuccess: ((t: T) -> Unit),
//            onError: ((t: Throwable) -> Unit),
//            params: Params
//    ) {
//        disposeLast()
//        lastDisposable = buildUseCaseSingle(params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(onSuccess, onError)
//        lastDisposable?.let { compositeDisposable.add(it) }
//    }
// }
//
