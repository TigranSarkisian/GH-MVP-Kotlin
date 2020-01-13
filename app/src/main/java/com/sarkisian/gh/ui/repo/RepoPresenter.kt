package com.sarkisian.gh.ui.repo


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.ui.base.mvp.BasePresenter
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.extensions.addTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class RepoPresenter @Inject constructor(
    private val repository: GitHubDataSource,
    private val errorHandler: ErrorHandler
) : BasePresenter<RepoContract.RepoView>(), RepoContract.RepoPresenter {

    override fun loadRepo(gitHubUser: String?, repoName: String?) {
        Observable.just(Pair(gitHubUser, repoName))
            .filter { it.first != null && it.second != null }
            .firstOrError()
            .flatMap { repository.getRepo(it.first!!, it.second!!) }
            .doOnSubscribe { view?.showLoadingIndicator(true) }
            .observeOn(AndroidSchedulers.mainThread())
            .filter { !it.isEmpty() }
            .toSingle()
            .map { it.get() }
            .doAfterTerminate { view?.showLoadingIndicator(false) }
            .subscribe({
                view?.onRepoLoaded(it!!)
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

    override fun deleteRepo(repo: Repo) {
        repository.deleteRepo(repo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onRepoDeleted(repo)
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

    override fun updateRepo(repo: Repo) {
        repository.insertOrUpdateRepos(listOf(repo))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onRepoUpdated(repo)
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

    override fun addRepoToFavorites(repo: Repo) {
        repository.addRepoToFavorites(repo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.i("${repo.name} added to favorites")
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

    override fun deleteRepoFromFavorites(repo: Repo) {
        repository.deleteRepoFromFavorites(repo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.i("${repo.name} removed from favorites")
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

}
