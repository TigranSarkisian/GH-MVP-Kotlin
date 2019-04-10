package com.sarkisian.gh.data.repository

import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import com.sarkisian.gh.util.rxbus.RxBus
import com.sarkisian.gh.util.rxbus.RxEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class GitHubRepository @Inject constructor(
        private val localDataSource: GitHubLocalDataSource,
        private val remoteDataSource: GitHubRemoteDataSource,
        private val rxBus: RxBus
) : GitHubDataSource {

    private var forceUpdate: Boolean = false

    override fun refreshRepos() {
        forceUpdate = true
    }

    override fun getRepo(gitHubUser: String, repoName: String): Observable<Repo> {
        val localData = localDataSource.getRepo(gitHubUser, repoName)
                .subscribeOn(Schedulers.io())

        val remoteData = remoteDataSource.getRepo(gitHubUser, repoName)
                .subscribeOn(Schedulers.io())
                .flatMap {
                    val localRepo = localDataSource.getRepo(gitHubUser, repoName).blockingSingle()
                    if (localRepo.favorite) it.favorite = localRepo.favorite
                    localDataSource.addRepo(it)
                    return@flatMap localDataSource.getRepo(gitHubUser, repoName)
                }

        return Observable.concatArrayDelayError(
                localData.observeOn(AndroidSchedulers.mainThread()),
                remoteData.observeOn(AndroidSchedulers.mainThread()))
    }

    override fun getRepos(gitHubUser: String, page: Int): Observable<MutableList<Repo>> {
        when (page) {
            // First page
            1 -> {
                val localData = localDataSource.getRepos(gitHubUser)
                        .subscribeOn(Schedulers.io())
                        .doOnNext {
                            if (it.isEmpty()) forceUpdate = true
                        }

                return if (forceUpdate) {
                    val remoteData = remoteDataSource.getRepos(gitHubUser)
                            .subscribeOn(Schedulers.io())
                            .flatMap {
                                localDataSource.getFavoriteRepos(gitHubUser).blockingSingle().forEach { localRepo ->
                                    if (localRepo.favorite) {
                                        val necessaryRemoteRepo = it.find { remoteRepo -> localRepo.id == remoteRepo.id }
                                        necessaryRemoteRepo?.favorite = localRepo.favorite
                                    }
                                }
                                localDataSource.addRepos(it)
                                forceUpdate = false
                                return@flatMap localDataSource.getRepos(gitHubUser)
                            }
                            .doOnComplete {
                                rxBus.post(RxEvent.UpdateRepoList(gitHubUser))
                            }

                    Observable.concatArrayDelayError(
                            localData.observeOn(AndroidSchedulers.mainThread()),
                            remoteData.observeOn(AndroidSchedulers.mainThread()))
                } else {
                    localData
                            .observeOn(AndroidSchedulers.mainThread())
                }
            }
            else -> return remoteDataSource.getRepos(gitHubUser, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun searchRepos(query: String, page: Int): Observable<SearchRequest> {
        return remoteDataSource.searchRepos(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addRepo(repo: Repo): Completable {
        return localDataSource.addRepo(repo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addRepos(repos: List<Repo>): Completable {
        return localDataSource.addRepos(repos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteRepo(repo: Repo): Completable {
        return localDataSource.deleteRepo(repo)
                .doOnComplete {
                    rxBus.post(RxEvent.DeleteRepo(repo))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateRepo(repo: Repo): Completable {
        return localDataSource.updateRepo(repo)
                .doOnComplete {
                    rxBus.post(RxEvent.UpdateRepo(repo))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addRepoToFavorites(repo: Repo): Completable {
        return localDataSource.addRepoToFavorites(repo)
                .doOnComplete {
                    rxBus.post(RxEvent.FavoriteRepo(repo))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteRepoFromFavorites(repo: Repo): Completable {
        return localDataSource.deleteRepoFromFavorites(repo)
                .doOnComplete {
                    rxBus.post(RxEvent.FavoriteRepo(repo))
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFavoriteRepos(gitHubUser: String): Observable<MutableList<Repo>> {
        return localDataSource.getFavoriteRepos(gitHubUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}
