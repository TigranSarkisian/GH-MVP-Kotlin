package com.sarkisian.gh.data.api


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubAPI {

    @GET("users/{username}/repos")
    fun getRepos(
            @Path("username") username: String,
            @Query("client_id") client_id: String = "5301f7f2e5e82214454b",
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 30,
            @Query("client_secret") client_secret: String = "00b3dc2b7c8f9641d71c569a961acbbddf497f8d"
    ): Observable<MutableList<Repo>>

    @GET("repos/{username}/{repo_name}")
    fun getRepo(
            @Path("username") username: String,
            @Path("repo_name") repoName: String,
            @Query("client_id") client_id: String = "5301f7f2e5e82214454b",
            @Query("client_secret") client_secret: String = "00b3dc2b7c8f9641d71c569a961acbbddf497f8d"
    ): Observable<Repo>

    @GET("search/repositories")
    fun searchRepos(
            @Query("client_id") client_id: String = "5301f7f2e5e82214454b",
            @Query("client_secret") client_secret: String = "00b3dc2b7c8f9641d71c569a961acbbddf497f8d",
            @Query("q") query: String,
            @Query("page") page: Int = 1,
            @Query("per_page") per_page: Int = 30
    ): Observable<SearchRequest>

}
