package com.sarkisian.gh.data.api


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubAPI {

    @GET("users/{username}/repos")
    fun loadRepos(
        @Path("username") username: String,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = 30
    ): Observable<MutableList<Repo>>

    @GET("repos/{username}/{repo_name}")
    fun loadRepo(
        @Path("username") username: String,
        @Path("repo_name") repoName: String
    ): Observable<Repo>

    @GET("search/repositories")
    fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = 30
    ): Observable<SearchRequest>

}
