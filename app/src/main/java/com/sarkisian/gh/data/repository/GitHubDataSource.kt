package com.sarkisian.gh.data.repository

import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import io.reactivex.Completable
import io.reactivex.Observable


interface GitHubDataSource {

    fun getRepos(gitHubUser: String, page: Int = 1): Observable<MutableList<Repo>>

    fun addRepos(repos: List<Repo>): Completable

    fun getRepo(gitHubUser: String, repoName: String): Observable<Repo>

    fun addRepo(repo: Repo): Completable

    fun deleteRepo(repo: Repo): Completable

    fun updateRepo(repo: Repo): Completable

    fun addRepoToFavorites(repo: Repo): Completable

    fun deleteRepoFromFavorites(repo: Repo): Completable

    fun getFavoriteRepos(gitHubUser: String): Observable<MutableList<Repo>>

    fun searchRepos(query: String, page: Int): Observable<SearchRequest>

    fun refreshRepos()

}