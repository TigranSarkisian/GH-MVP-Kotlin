package com.sarkisian.gh.data.api


import com.sarkisian.gh.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiFactory {

    const val BASE_URL = "https://api.github.com/"
    const val GIT_HUB_USER = "google"

    @JvmStatic
    fun getGitHubAPI(baseUrl: String): GitHubAPI {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .writeTimeout(10L, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            httpClient.addInterceptor(logging)
        }

        val okHttpClient = httpClient.build()

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

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubAPI::class.java)
    }

}
