package com.sarkisian.gh.data.repository

import com.sarkisian.gh.data.api.GitHubAPI
import com.sarkisian.gh.data.db.RealmFactory
import com.sarkisian.gh.data.entity.Optional
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class GitHubRepository @Inject constructor(
    private val gitHubAPI: GitHubAPI
) : GitHubDataSource {

    override fun getRepo(gitHubUser: String, repoName: String): Single<Optional<Repo>> =
        gitHubAPI.loadRepo(gitHubUser, repoName)
            .subscribeOn(Schedulers.io())
            .map { Optional(it) }
            .onErrorReturn { Optional(RealmFactory.getRepo(repoName)) }

    override fun getRepos(gitHubUser: String): Single<MutableList<Repo>> =
        gitHubAPI.loadRepos(username = gitHubUser)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { RealmFactory.insertOrUpdateRepos(it) }
            .onErrorReturn { RealmFactory.getRepos() }

    override fun searchRepos(query: String): Single<SearchRequest> =
        gitHubAPI.searchRepos(query = query)
            .subscribeOn(Schedulers.io())

    override fun insertOrUpdateRepos(repos: List<Repo>): Completable =
        Completable.fromCallable { RealmFactory.insertOrUpdateRepos(repos) }
            .subscribeOn(Schedulers.computation())

    override fun deleteRepo(repo: Repo): Completable =
        Completable.fromCallable { RealmFactory.deleteRepo(repo) }
            .subscribeOn(Schedulers.computation())

    override fun addRepoToFavorites(repo: Repo): Completable =
        Observable.just(repo)
            .subscribeOn(Schedulers.computation())
            .map { RealmFactory.insertOrUpdateRepos(repo) }
            .ignoreElements()

    override fun deleteRepoFromFavorites(repo: Repo): Completable =
        Observable.just(repo)
            .subscribeOn(Schedulers.computation())
            .map { it.favorite = false }
            .map { RealmFactory.insertOrUpdateRepos(repo) }
            .ignoreElements()

    override fun getFavoriteRepos(): Observable<MutableList<Repo>> =
        Observable.fromCallable { RealmFactory.getFavoriteRepos() }
            .subscribeOn(Schedulers.computation())

}
