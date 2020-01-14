package com.sarkisian.gh.data.api


import com.sarkisian.gh.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkFactory {

    fun <T> buildRetrofit(
        clazz: Class<T>,
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(clazz)
    }

    fun buildHttpClient(
        timeout: Long = 5
    ): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }

        /*if (auth_required) {
            // First option
            httpClient.addNetworkInterceptor(AuthInterceptor())

            // Second option
            httpClient.interceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                        .header("Authorization", "Basic ***")
                        .method(original.method(), original.body())
                        .build()

                chain.proceed(request)
            })
        }*/

        return httpClient.build()
    }

}
