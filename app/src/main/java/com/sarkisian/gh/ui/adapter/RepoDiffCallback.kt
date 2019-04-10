package com.sarkisian.gh.ui.adapter

import android.support.v7.util.DiffUtil
import com.sarkisian.gh.data.entity.Repo

class RepoDiffCallback(private val oldList: List<Repo>,
                       private val newList: List<Repo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = oldList[oldItemPosition] == newList[newItemPosition]
}