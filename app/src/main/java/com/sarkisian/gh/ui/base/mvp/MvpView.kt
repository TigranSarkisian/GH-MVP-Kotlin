package com.sarkisian.gh.ui.base.mvp


interface MvpView {

    fun showMessage(message: String) {}

    fun showEmptyState(value: Boolean) {}

    fun showLoadingIndicator(value: Boolean) {}

}
