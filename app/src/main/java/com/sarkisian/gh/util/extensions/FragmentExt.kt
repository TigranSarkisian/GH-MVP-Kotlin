package com.sarkisian.gh.util.extensions

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun Fragment.toast(message: String?) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.name(): String {
    return this.javaClass.simpleName
}

fun Fragment.snack(msg: String) {
    this.view?.let {
        Snackbar.make(it, msg, Snackbar.LENGTH_SHORT).show()
    }
}
