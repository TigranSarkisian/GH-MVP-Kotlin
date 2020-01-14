package com.sarkisian.gh.data.api


import com.sarkisian.gh.data.api.NetworkFactory.buildHttpClient
import com.sarkisian.gh.data.api.NetworkFactory.buildRetrofit

object ApiFactory {

    private const val BASE_URL = "https://api.github.com/"

    val gitHubAPI: GitHubAPI = buildRetrofit(
        GitHubAPI::class.java,
        BASE_URL,
        buildHttpClient()
    )

}
