package com.sarkisian.gh.util


import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import java.nio.charset.Charset

class GsonUtil {

    fun <T> readJson(fileName: String, inClass: Class<T>): T =
            GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create()
                    .fromJson(readString(fileName), inClass)

    fun readString(fileName: String): String? =
        javaClass.classLoader.getResourceAsStream(fileName).use { stream ->
                stream.readBytes().toString(Charset.defaultCharset())
            }
}
