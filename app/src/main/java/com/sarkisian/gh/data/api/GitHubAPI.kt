package com.sarkisian.gh.data.api


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubAPI {

    @GET("users/{username}/repos")
    fun loadRepos(
        @Path("username") username: String,
        @Query("client_id") client_id: String = "5301f7f2e5e82214454b",
        @Query("client_secret") client_secret: String = "00b3dc2b7c8f9641d71c569a961acbbddf497f8d"
    ): Single<MutableList<Repo>>

    @GET("repos/{username}/{repo_name}")
    fun loadRepo(
        @Path("username") username: String,
        @Path("repo_name") repoName: String,
        @Query("client_id") client_id: String = "5301f7f2e5e82214454b",
        @Query("client_secret") client_secret: String = "00b3dc2b7c8f9641d71c569a961acbbddf497f8d"
    ): Single<Repo>

    @GET("search/repositories")
    fun searchRepos(
        @Query("client_id") client_id: String = "5301f7f2e5e82214454b",
        @Query("client_secret") client_secret: String = "00b3dc2b7c8f9641d71c569a961acbbddf497f8d",
        @Query("q") query: String
    ): Single<SearchRequest>

}
