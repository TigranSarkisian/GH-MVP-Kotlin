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


class GitHubRepository constructor(
    private val gitHubAPI: GitHubAPI
) : GitHubDataSource {

    override fun getRepo(gitHubUser: String, repoName: String): Single<Optional<Repo>> {
        return gitHubAPI.loadRepo(gitHubUser, repoName)
            .subscribeOn(Schedulers.io())
            .map { Optional(it) }
            .onErrorReturn { Optional(RealmFactory.getRepo(repoName)) }
    }

    override fun getRepos(gitHubUser: String): Single<MutableList<Repo>> {
        return gitHubAPI.loadRepos(username = gitHubUser)
            .subscribeOn(Schedulers.io())
            .doOnSuccess { RealmFactory.insertOrUpdateRepos(it) }
            .onErrorReturn { RealmFactory.getRepos() }
    }

    override fun searchRepos(query: String): Single<SearchRequest> =
        gitHubAPI.searchRepos(query = query)
            .subscribeOn(Schedulers.io())

    override fun insertOrUpdateRepos(repos: List<Repo>): Completable {
        return Completable.fromCallable { RealmFactory.insertOrUpdateRepos(repos) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteRepo(repo: Repo): Completable {
        return Completable.fromCallable { RealmFactory.deleteRepo(repo) }
            .subscribeOn(Schedulers.io())
    }

    override fun addRepoToFavorites(repo: Repo): Completable {
        return Completable.fromCallable { RealmFactory.insertOrUpdateRepos(repo) }
            .subscribeOn(Schedulers.io())
    }

    override fun deleteRepoFromFavorites(repo: Repo): Completable {
        return Observable.just(repo)
            .map { it.favorite = false }
            .map { RealmFactory.insertOrUpdateRepos(repo) }
            .ignoreElements()
            .subscribeOn(Schedulers.io())
    }

    override fun getFavoriteRepos(gitHubUser: String): Observable<MutableList<Repo>> {
        return Observable.just(RealmFactory.getFavoriteRepos())
            .subscribeOn(Schedulers.io())
    }

}
