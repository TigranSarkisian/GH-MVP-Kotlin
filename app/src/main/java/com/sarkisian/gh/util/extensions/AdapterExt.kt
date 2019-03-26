package com.sarkisian.gh.util.extensions

import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.adapter.RepoAdapter
import com.sarkisian.gh.ui.base.BaseAdapter
import timber.log.Timber


fun BaseAdapter<Repo, RepoAdapter.ViewHolder>.insertItems(positionStart: Int, items: MutableList<Repo>) {
    val itemsCount = this.items.size
    items.sortByDescending { repo: Repo? -> repo?.id }
    this.items.addAll(items)
    if (positionStart != -1 && itemsCount != -1) {
        notifyItemRangeInserted(positionStart, itemsCount)
    } else {
        throw Exception("Failed to insert items in list")
    }
}

fun BaseAdapter<Repo, RepoAdapter.ViewHolder>.insertItemOnTop(item: Repo): Int {
    this.items.add(0, item)
    val necessaryItem = this.items.find { item.id == it.id }
    val position = this.items.indexOf(necessaryItem)
    if (position != -1) {
        notifyItemInserted(position)
        return position
    } else {
        throw Exception("Failed to insert item in list")
    }
}

fun BaseAdapter<Repo, RepoAdapter.ViewHolder>.insertItem(item: Repo): Int {
    this.items.add(item)
    this.items.sortByDescending { repo: Repo? -> repo?.id }
    val necessaryItem = this.items.find { item.id == it.id }
    val position = this.items.indexOf(necessaryItem)
    if (position != -1) {
        notifyItemInserted(position)
        return position
    } else {
        throw Exception("Failed to insert item in list")
    }
}

fun BaseAdapter<Repo, RepoAdapter.ViewHolder>.removeItem(item: Repo) {
    val necessaryItem = this.items.find { item.id == it.id }
    val position = this.items.indexOf(necessaryItem)
    if (position != -1) {
        this.items.remove(necessaryItem)
        notifyItemRemoved(position)
    } else {
        Timber.d("Item not found in list")
    }
}

fun BaseAdapter<Repo, RepoAdapter.ViewHolder>.updateItem(item: Repo) {
    val necessaryItem = this.items.find { item.id == it.id }
    val position = this.items.indexOf(necessaryItem)
    if (position != -1) {
        this.items[position] = item
        notifyItemChanged(position)
    } else {
        Timber.d("Item not found in list")
    }
}