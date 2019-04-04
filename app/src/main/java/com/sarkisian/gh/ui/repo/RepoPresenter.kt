package com.sarkisian.gh.ui.repo


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.ui.base.mvp.BasePresenter
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.error.ObjectNotFoundException
import com.sarkisian.gh.util.extensions.addTo
import timber.log.Timber

class RepoPresenter constructor(
    private val gitHubRepository: GitHubDataSource,
    private val errorHandler: ErrorHandler
) : BasePresenter<RepoContract.RepoView>(), RepoContract.RepoPresenter {

    override fun loadRepo(gitHubUser: String, repoName: String) {
        gitHubRepository.getRepo(gitHubUser, repoName)
            .doOnSubscribe { view?.showLoadingIndicator(true) }
            .doAfterTerminate { view?.showLoadingIndicator(false) }
            .subscribe(
                { view?.showRepo(it) },
                { it ->
                    when (it) {
                        is ObjectNotFoundException -> errorHandler.readError(it) {
                            view?.showMessage(it)
                        }
                        else -> errorHandler.readError(it) { view?.showMessage(it) }
                    }
                }
            )
            .addTo(compositeDisposable)
    }

    override fun deleteRepo(repo: Repo) {
        gitHubRepository.deleteRepo(repo)
            .subscribe(
                { view?.showRepoDeleted(repo) },
                { it -> errorHandler.readError(it) { view?.showMessage(it) } }
            )
            .addTo(compositeDisposable)
    }

    override fun updateRepo(repo: Repo) {
        gitHubRepository.updateRepo(repo)
            .subscribe(
                { view?.showRepoUpdated(repo) },
                { it -> errorHandler.readError(it) { view?.showMessage(it) } }
            )
            .addTo(compositeDisposable)
    }

    override fun addRepoToFavorites(repo: Repo) {
        gitHubRepository.addRepoToFavorites(repo)
            .subscribe(
                { Timber.e("${repo.name} added to favorites") },
                { it -> errorHandler.readError(it) { view?.showMessage(it) } }
            )
            .addTo(compositeDisposable)
    }

    override fun deleteRepoFromFavorites(repo: Repo) {
        gitHubRepository.deleteRepoFromFavorites(repo)
            .subscribe(
                { Timber.e("${repo.name} removed from favorites") },
                { it -> errorHandler.readError(it) { view?.showMessage(it) } }
            )
            .addTo(compositeDisposable)
    }

}
