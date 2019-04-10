package com.sarkisian.gh.data.repository

import com.sarkisian.gh.data.entity.Optional
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single


interface GitHubDataSource {

    fun getRepo(gitHubUser: String, repoName: String): Single<Optional<Repo>>

    fun getRepos(gitHubUser: String): Single<MutableList<Repo>>

    fun insertOrUpdateRepos(repos: List<Repo>): Completable

    fun deleteRepo(repo: Repo): Completable

    fun addRepoToFavorites(repo: Repo): Completable

    fun deleteRepoFromFavorites(repo: Repo): Completable

    fun getFavoriteRepos(gitHubUser: String): Observable<MutableList<Repo>>

    fun searchRepos(query: String): Single<SearchRequest>

}