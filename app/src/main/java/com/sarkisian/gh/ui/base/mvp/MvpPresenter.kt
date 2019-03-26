package com.sarkisian.gh.ui.base.mvp

interface MvpPresenter<in View : MvpView> {

    fun attachView(view: View)

    fun detachView()

}

