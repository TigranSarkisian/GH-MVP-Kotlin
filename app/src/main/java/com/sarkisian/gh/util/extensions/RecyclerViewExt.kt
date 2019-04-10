package com.sarkisian.gh.util.extensions

import android.support.v7.widget.RecyclerView


fun RecyclerView.onScrollToBottom(doAction: (Unit) -> Unit)
        = addOnScrollListener(object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (!recyclerView.canScrollVertically(-1) && (dy < 0)) {
            // top
        } else if (!recyclerView.canScrollVertically(1) && (dy > 0)) {
            // bottom
            doAction(Unit)
        }

        if (dy < 0) {
            // scrolling to top
        } else if (dy > 0) {
            // scrolling to bottom
        }
    }
})

fun RecyclerView.ifNotScrollable(doAction: (Unit) -> Unit) {
    if (this.computeVerticalScrollRange() <= this.height) {
        doAction(Unit)
    }
}