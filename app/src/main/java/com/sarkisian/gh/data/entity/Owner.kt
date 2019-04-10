package com.sarkisian.gh.data.entity


import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Owner(

    @PrimaryKey
    @SerializedName("id")
    var id: Long? = null,

    @SerializedName("login")
    var login: String? = null

) : RealmObject() {

    companion object {
        const val OWNER_ID: String = "id"
        const val OWNER_LOGIN: String = "login"
    }

}