package com.sarkisian.gh.ui.favorites


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.ui.base.mvp.BasePresenter
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.extensions.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class RepoFavoritesPresenter @Inject constructor(
    private val repository: GitHubDataSource,
    private val errorHandler: ErrorHandler
) : BasePresenter<RepoFavoritesContract.RepoFavoritesView>(), RepoFavoritesContract.RepoFavoritesPresenter {

    override fun getFavoriteRepos() {
        repository.getFavoriteRepos()
            .doOnSubscribe { view?.showLoadingIndicator(true) }
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { view?.showLoadingIndicator(false) }
            .subscribe({
                view?.onFavoriteReposLoaded(it)
                view?.showEmptyState(it.isEmpty())
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

    override fun deleteRepoFromFavorites(repo: Repo) {
        repository.deleteRepoFromFavorites(repo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.onRepoDeletedFromFavorites(repo)
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

}
