package com.sarkisian.gh.data.entity

class Optional<M>(private val optional: M?) {

    fun isEmpty(): Boolean = optional == null

    fun get(): M? = optional

}
