package com.sarkisian.gh.ui.repos


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.ui.base.mvp.BasePresenter
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.extensions.addTo
import io.reactivex.android.schedulers.AndroidSchedulers

class RepoListPresenter constructor(
    private val gitHubRepository: GitHubDataSource,
    private val errorHandler: ErrorHandler
) : BasePresenter<RepoListContract.RepoListView>(), RepoListContract.RepoListPresenter {

    override fun loadRepos(username: String) {
        gitHubRepository.getRepos(username)
            .doOnSubscribe { view?.showLoadingIndicator(true) }
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { view?.showLoadingIndicator(false) }
            .subscribe({
                view?.onReposLoaded(it)
                view?.showEmptyState(it.isEmpty())
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

    override fun deleteRepo(repo: Repo) {
        gitHubRepository.deleteRepo(repo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { view?.onRepoDeleted(repo) }
            .addTo(compositeDisposable)
    }

    override fun addRepo(repo: Repo) {
        gitHubRepository.insertOrUpdateRepos(listOf(repo))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { view?.onRepoAdded(repo) }
            .addTo(compositeDisposable)
    }

    override fun updateRepo(repo: Repo) {
        gitHubRepository.insertOrUpdateRepos(listOf(repo))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { view?.onRepoUpdated(repo) }
            .addTo(compositeDisposable)
    }

}
