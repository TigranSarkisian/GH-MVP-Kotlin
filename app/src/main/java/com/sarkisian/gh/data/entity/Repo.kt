package com.sarkisian.gh.data.entity


import com.google.gson.annotations.SerializedName
import com.sarkisian.gh.util.extensions.formatTimeMillisToISO
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Repo(

    @PrimaryKey
    @SerializedName("id")
    var id: Long? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("html_url")
    var htmlUrl: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("language")
    var language: String? = null,

    @SerializedName("stargazersCount")
    var stargazersCount: Int? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null,

    @SerializedName("network_count")
    var networkCount: Int? = null,

    @SerializedName("subscribers_count")
    var subscribersCount: Int? = null,

    @SerializedName("owner")
    var owner: Owner? = null,

    var favorite: Boolean = false

) : RealmObject() {

    companion object {

        const val REPO_ID: String = "id"
        const val REPO_NAME: String = "name"
        const val REPO_IS_FAVORITE: String = "favorite"

        fun composeStubRepo() = Repo(
            id = System.currentTimeMillis(),
            name = "Repo ${System.currentTimeMillis()}",
            language = "Kotlin",
            htmlUrl = "http://www.nourl.com/",
            description = "This is test repo created with id ${System.currentTimeMillis()}",
            updatedAt = System.currentTimeMillis().formatTimeMillisToISO()
        )
    }

}