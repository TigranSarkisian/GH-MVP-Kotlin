package com.sarkisian.gh.util.extensions

import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView


fun View.isVisible(): Boolean {
    return when (visibility) {
        View.VISIBLE -> true
        else -> false
    }
}

fun View.visible(value: Boolean) {
    this.visibility = when (value) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

fun <T> TextView.setTextWithFormatArgs(@StringRes textWithFormatArgs: Int, input: T?) {
    when (input) {
        null -> this.visible(false)
        is String -> {
            this.visible(!input.isBlank())
            this.text = resources.getString(textWithFormatArgs, input)
        }
        else -> {
            this.visible(true)
            this.text = resources.getString(textWithFormatArgs, input.toString())
        }
    }
}

fun <T> TextView.setTextOrHide(input: T) {
    when (input) {
        null -> this.visible(false)
        is String -> {
            this.visible(!input.isBlank())
            this.text = input
        }
        else -> {
            this.visible(true)
            this.text = input.toString()
        }
    }
}
