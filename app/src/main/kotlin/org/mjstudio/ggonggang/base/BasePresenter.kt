package org.mjstudio.ggonggang.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface BasePresenter<V : BaseView> {
    var view: V?
    var compositeDisposable: CompositeDisposable?

    fun attachView(v: V) {
        compositeDisposable = CompositeDisposable()
        view = v
    }
    fun detachView() {
        compositeDisposable?.clear()
        compositeDisposable?.dispose()
        view = null
    }
}

operator fun CompositeDisposable?.plus(d: Disposable): CompositeDisposable? {
    this?.add(d)
    return this
}