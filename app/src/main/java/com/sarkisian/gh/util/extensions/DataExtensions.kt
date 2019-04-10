package com.sarkisian.gh.util.extensions

import android.support.design.widget.Snackbar
import android.view.View
import com.sarkisian.gh.data.entity.Repo
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun emptyString(): String {
    return ""
}

fun emptyRepo(): Repo {
    return Repo()
}