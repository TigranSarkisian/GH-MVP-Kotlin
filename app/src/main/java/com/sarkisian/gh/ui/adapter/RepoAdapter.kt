package com.sarkisian.gh.ui.adapter

import android.support.v7.util.DiffUtil
import android.view.View
import com.sarkisian.gh.R
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.BaseAdapter
import com.sarkisian.gh.ui.base.BaseViewHolder
import com.sarkisian.gh.util.extensions.formatDate
import com.sarkisian.gh.util.extensions.setTextOrHide
import com.sarkisian.gh.util.extensions.setTextWithFormatArgs
import com.sarkisian.gh.util.extensions.visible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.repo_item.view.*


class RepoAdapter(
    private val onItemClickListener: OnItemClickListener
) : BaseAdapter<Repo, RepoAdapter.ViewHolder>() {

    override fun getItemViewId(): Int = R.layout.repo_item

    override fun getViewHolder(view: View): ViewHolder = ViewHolder(view, onItemClickListener)

    fun setItems(items: MutableList<Repo>, onComplete: () -> Unit): Disposable {
        var newList: MutableList<Repo> = mutableListOf()
        return Observable.just(items)
            .subscribeOn(Schedulers.io())
            .doOnNext { newList = it }
            .map { DiffUtil.calculateDiff(RepoDiffCallback(this.items, it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { this.items = newList }
            .doAfterTerminate { onComplete() }
            .subscribe { it.dispatchUpdatesTo(this) }
    }

    class ViewHolder(
        itemView: View,
        private var onItemClickListener: OnItemClickListener
    ) : BaseViewHolder<Repo>(itemView) {

        override fun onBind(item: Repo) = with(itemView) {
            tv_repo_title.setTextOrHide(item.name)
            iv_repo_fav.visible(item.favorite)
            tv_repo_updated_at.setTextWithFormatArgs(R.string.updated_at, item.updatedAt?.formatDate())
            tv_repo_description.setTextOrHide(item.description)

            setOnClickListener {
                onItemClickListener.onItemClick(adapterPosition, item)
            }
            setOnLongClickListener {
                onItemClickListener.onItemLongClick(adapterPosition, item)
                true
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, repo: Repo)
        fun onItemLongClick(position: Int, repo: Repo)
    }

}

