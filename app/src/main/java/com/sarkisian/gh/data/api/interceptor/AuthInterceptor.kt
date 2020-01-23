package com.sarkisian.gh.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .addHeader("Authorization", "token a6a6b58fa65e18eb344d952a2ab782f207a0987a")
            .method(original.method(), original.body())
            .build()

        return chain.proceed(request)
    }

}