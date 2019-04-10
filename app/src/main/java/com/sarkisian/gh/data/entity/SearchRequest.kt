package com.sarkisian.gh.data.entity

import com.google.gson.annotations.SerializedName

data class SearchRequest(

    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @SerializedName("items")
    val items: MutableList<Repo>

)