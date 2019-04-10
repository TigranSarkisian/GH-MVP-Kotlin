package com.sarkisian.gh.ui.favorites


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.ui.base.mvp.BasePresenter
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.extensions.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class RepoFavoritesPresenter @Inject constructor(
    private val gitHubRepository: GitHubDataSource,
    private val errorHandler: ErrorHandler
) : BasePresenter<RepoFavoritesContract.RepoFavoritesView>(), RepoFavoritesContract.RepoFavoritesPresenter {

    override fun loadFavoriteRepos(username: String) {
        gitHubRepository.getFavoriteRepos(username)
            .doOnSubscribe { view?.showLoadingIndicator(true) }
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { view?.showLoadingIndicator(false) }
            .subscribe({
                view?.showFavoriteRepos(it)
                view?.showEmptyState(it.isEmpty())
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

    override fun deleteRepoFromFavorites(repo: Repo) {
        gitHubRepository.deleteRepoFromFavorites(repo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.showRepoDeletedFromFavorites(repo)
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

}
