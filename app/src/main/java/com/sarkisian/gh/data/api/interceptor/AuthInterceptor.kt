package com.sarkisian.gh.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("Authorization", "Basic ***")
            .method(original.method(), original.body())
            .build()
        return chain.proceed(request)
    }

    /*if (auth_required) {
         httpClient.interceptors().add(Interceptor { chain ->
             val original = chain.request()
             val request = original.newBuilder()
                     .header("Authorization", "Basic ***")
                     .method(original.method(), original.body())
                     .build()

             chain.proceed(request)
         })
     }*/

}