package com.sarkisian.gh.util.extensions

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.SerialDisposable
import timber.log.Timber

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun Disposable.setTo(serialDisposable: SerialDisposable) {
    serialDisposable.set(this)
}

fun <T> Observable<T>.subscribeWithDispose(
    result: (T) -> Unit = { },
    error: (Throwable) -> Unit = { }
) {
    var disposable: Disposable? = null
    disposable = this
        .doFinally {
            disposable?.let { if (!it.isDisposed) it.dispose() }
        }
        .subscribe({
            result(it)
        }, {
            Timber.e(it)
            error(it)
        })
}

fun <T> Single<T>.subscribeWithDispose(
    result: (T) -> Unit = { },
    error: (Throwable) -> Unit = { }
) {
    var disposable: Disposable? = null
    disposable = this
        .doFinally {
            disposable?.let { if (!it.isDisposed) it.dispose() }
        }
        .subscribe({
            result(it)
        }, {
            Timber.e(it)
            error(it)
        })
}

fun Completable.subscribeWithDispose(
    result: () -> Unit = { },
    error: (Throwable) -> Unit = { }
) {
    var disposable: Disposable? = null
    disposable = this
        .doFinally {
            disposable?.let { if (!it.isDisposed) it.dispose() }
        }
        .subscribe({
            result()
        }, {
            Timber.e(it)
            error(it)
        })
}
