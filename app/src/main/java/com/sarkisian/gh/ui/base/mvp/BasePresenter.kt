package com.sarkisian.gh.ui.base.mvp

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<View : MvpView> : MvpPresenter<View> {

    var view: View? = null
    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun attachView(view: View) {
        this.view = view
    }

    override fun detachView() {
        compositeDisposable.clear()
        this.view = null
    }

}
