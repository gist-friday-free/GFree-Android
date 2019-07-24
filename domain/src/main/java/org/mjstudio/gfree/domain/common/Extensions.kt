package org.mjstudio.gfree.domain.common

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.mjstudio.gfree.domain.BuildConfig
import java.util.concurrent.*

//region RXJAVA

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> Single<T>.addSchedulers(): Single<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
fun <T> Maybe<T>.addSchedulers(): Maybe<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
fun <T> Observable<T>.addSchedulers(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
fun <T> Flowable<T>.addSchedulers(): Flowable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}
fun Completable.addSchedulers(): Completable {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun Completable.addComputationSchedulers(): Completable {
    return this.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}

fun rxSingleTimer(milliseconds: Long, completion: (t: Long) -> Unit): Disposable {
    return Single.timer(milliseconds, TimeUnit.MILLISECONDS)
            .addSchedulers()
            .subscribe(completion)
}

fun rxSimpleComputationRun(run: () -> Unit): Disposable {
    return Completable.fromCallable(run)
            .addComputationSchedulers()
            .subscribe()
}
fun rxSimpleRun(run: () -> Unit): Disposable {
    return Completable.fromCallable(run)
            .addSchedulers()
            .subscribe()
}
//endregion
fun debugE(tag: String, message: Any?) {
    if (BuildConfig.DEBUG)
        Log.e(tag, "\uD83C\uDF40" + message.toString())
}
fun debugE(message: Any?) {

    debugE("DEBUG", message)
}