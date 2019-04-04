package com.sarkisian.gh.data.repository


import com.sarkisian.gh.data.api.GitHubAPI
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.entity.SearchRequest
import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber

class GitHubRemoteDataSource constructor(
    private val gitHubAPI: GitHubAPI
) : GitHubDataSource {

    override fun getRepo(gitHubUser: String, repoName: String): Observable<Repo> {
        return gitHubAPI.getRepo(gitHubUser, repoName)
            .doOnNext {
                Timber.i("Loaded ${it.name} repo")
            }
    }

    override fun getRepos(gitHubUser: String, page: Int): Observable<MutableList<Repo>> {
        return gitHubAPI.getRepos(username = gitHubUser, page = page)
            .doOnNext {
                Timber.i("Loaded ${it.size} repos")
            }
    }

    override fun addRepo(repo: Repo): Completable = Completable.complete()

    override fun addRepos(repos: List<Repo>): Completable = Completable.complete()

    override fun updateRepo(repo: Repo): Completable = Completable.complete()

    override fun deleteRepo(repo: Repo): Completable = Completable.complete()

    override fun addRepoToFavorites(repo: Repo): Completable = Completable.complete()

    override fun deleteRepoFromFavorites(repo: Repo): Completable = Completable.complete()

    override fun getFavoriteRepos(gitHubUser: String): Observable<MutableList<Repo>> = Observable.empty()

    override fun searchRepos(query: String, page: Int): Observable<SearchRequest> {
        return gitHubAPI.searchRepos(query = query, page = page)
            .doOnNext {
                Timber.i("Search result: ${it.totalCount} repos")
            }
    }

    override fun refreshRepos() {
        // Not required, {@link GitHubRepository} handles the logic of
        // refreshing the repos from all the available data sources.
    }

}
