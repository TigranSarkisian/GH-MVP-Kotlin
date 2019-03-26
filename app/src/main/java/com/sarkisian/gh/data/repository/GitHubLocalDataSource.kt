package com.sarkisian.gh.data.repository


import com.sarkisian.gh.data.db.RealmFactory
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber

class GitHubLocalDataSource : GitHubDataSource {

    override fun getRepo(gitHubUser: String, repoName: String): Observable<Repo> {
        return Observable.just(RealmFactory.getRepo(repoName))
                .doOnNext {
                    Timber.i("Retrieved ${it.name} repo")
                }
    }

    override fun getRepos(gitHubUser: String, page: Int): Observable<MutableList<Repo>> {
        return Observable.just(RealmFactory.getRepos())
                .doOnNext {
                    Timber.i("Retrieved ${it.size} repos")
                }
    }

    override fun addRepos(repos: List<Repo>): Completable {
        return Single.just(RealmFactory.insertOrUpdateRepos(repos))
                .toCompletable()
                .doOnComplete {
                    Timber.i("Added ${repos.size} repos")
                }
    }

    override fun addRepo(repo: Repo): Completable {
        return Single.just(RealmFactory.insertOrUpdateRepos(repo))
                .toCompletable()
                .doOnComplete {
                    Timber.i("Added ${repo.name} repo")
                }
    }

    override fun updateRepo(repo: Repo): Completable {
        return Single.just(RealmFactory.insertOrUpdateRepos(repo))
                .toCompletable()
                .doOnComplete {
                    Timber.i("Updated ${repo.name} repo")
                }
    }

    override fun deleteRepo(repo: Repo): Completable {
        return Single.just(RealmFactory.deleteRepo(repo))
                .toCompletable()
                .doOnComplete {
                    Timber.i("Deleted ${repo.name} repo")
                }
    }

    override fun addRepoToFavorites(repo: Repo): Completable {
        return Single.just(RealmFactory.insertOrUpdateRepos(repo))
                .toCompletable()
                .doOnComplete {
                    Timber.i("Added ${repo.name} repo to favorites")
                }
    }

    override fun deleteRepoFromFavorites(repo: Repo): Completable {
        repo.favorite = false
        return Single.just(RealmFactory.insertOrUpdateRepos(repo))
                .toCompletable()
                .doOnComplete {
                    Timber.i("Deleted ${repo.name} repo from favorites")
                }
    }

    override fun getFavoriteRepos(gitHubUser: String): Observable<MutableList<Repo>> {
        return Observable.just(RealmFactory.getFavoriteRepos())
                .doOnNext {
                    Timber.i("Retrieved ${it.size} favorite repos")
                }
    }

    override fun refreshRepos() {
        // Not required because the {@link GitHubRepository} handles the logic of refreshing the
        // repos from all the available data sources.
    }

    override fun searchRepos(query: String, page: Int): Observable<SearchRequest> {
        return Observable.empty()
    }

}
