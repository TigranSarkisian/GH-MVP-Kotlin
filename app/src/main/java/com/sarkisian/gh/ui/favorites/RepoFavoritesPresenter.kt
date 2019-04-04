package com.sarkisian.gh.ui.favorites


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.ui.base.mvp.BasePresenter
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.extensions.addTo
import com.sarkisian.gh.util.rxbus.RxBus
import com.sarkisian.gh.util.rxbus.RxEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RepoFavoritesPresenter constructor(
    private val gitHubRepository: GitHubDataSource,
    private val errorHandler: ErrorHandler,
    private val rxBus: RxBus
) : BasePresenter<RepoFavoritesContract.RepoFavoritesView>(), RepoFavoritesContract.RepoFavoritesPresenter {

    override fun attachView(view: RepoFavoritesContract.RepoFavoritesView) {
        super.attachView(view)
        subscribeOnRxBusEvents()
    }

    override fun loadFavoriteRepos(username: String) {
        gitHubRepository.getFavoriteRepos(username)
            .doOnSubscribe { view?.showLoadingIndicator(true) }
            .doAfterTerminate { view?.showLoadingIndicator(false) }
            .subscribe(
                {
                    view?.showFavoriteRepos(it)
                    view?.showEmptyState(it.isEmpty())
                },
                { it -> errorHandler.readError(it) { view?.showMessage(it) } }
            )
            .addTo(compositeDisposable)
    }

    override fun deleteRepoFromFavorites(repo: Repo) {
        gitHubRepository.deleteRepoFromFavorites(repo)
            .subscribe(
                { view?.showRepoDeletedFromFavorites(repo) },
                { it -> errorHandler.readError(it) { view?.showMessage(it) } }
            )
            .addTo(compositeDisposable)
    }

    private fun subscribeOnRxBusEvents() {
        rxBus.observable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    is RxEvent.UpdateRepoList -> loadFavoriteRepos(it.gitHubUser)
                    is RxEvent.DeleteRepo -> view?.showRepoDeletedFromFavorites(it.repo)
                    is RxEvent.UpdateRepo -> view?.showRepoUpdated(it.repo)
                    is RxEvent.FavoriteRepo -> {
                        if (it.repo.favorite) {
                            view?.showRepoAddedToFavorites(it.repo)
                        } else {
                            view?.showRepoDeletedFromFavorites(it.repo)
                        }
                    }
                }
            }
            .addTo(compositeDisposable)
    }

}
