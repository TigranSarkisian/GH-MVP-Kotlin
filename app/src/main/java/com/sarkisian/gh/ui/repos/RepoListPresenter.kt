package com.sarkisian.gh.ui.repos


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.ui.base.mvp.BasePresenter
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.extensions.addTo
import com.sarkisian.gh.util.extensions.emptyString
import com.sarkisian.gh.util.rxbus.RxBus
import com.sarkisian.gh.util.rxbus.RxEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RepoListPresenter constructor(
    private val gitHubRepository: GitHubDataSource,
    private val errorHandler: ErrorHandler,
    private val rxBus: RxBus
) : BasePresenter<RepoListContract.RepoListView>(), RepoListContract.RepoListPresenter {

    private var loadingNextPage = false
    private var currentPage = 1
    private var username = emptyString()

    override fun attachView(view: RepoListContract.RepoListView) {
        super.attachView(view)
        subscribeOnRxBusEvents()
    }

    override fun loadRepos(username: String, forceUpdate: Boolean) {
        this.username = username

        if (forceUpdate) gitHubRepository.refreshRepos()

        gitHubRepository.getRepos(username)
            .doOnSubscribe { if (forceUpdate) view?.showLoadingIndicator(true) }
            .doAfterTerminate { if (forceUpdate) view?.showLoadingIndicator(false) }
            .subscribe({
                view?.showRepos(it)
                view?.showEmptyState(it.isEmpty())
                currentPage = 1
                loadingNextPage = false
            }, {
                errorHandler.readError(it) { view?.showMessage(it) }
            })
            .addTo(compositeDisposable)
    }

    override fun loadNextPage() {
        if (!loadingNextPage) {
            currentPage++

            gitHubRepository.getRepos(this.username, currentPage)
                .doOnSubscribe {
                    loadingNextPage = true
                    view?.showNextPageLoadingIndicator(true)
                }
                .doAfterTerminate {
                    loadingNextPage = false
                    view?.showNextPageLoadingIndicator(false)
                }
                .subscribe({
                    view?.showNextPageRepos(it)
                }, {
                    errorHandler.readError(it) { view?.showMessage(it) }
                })
                .addTo(compositeDisposable)
        }
    }

    override fun deleteRepo(repo: Repo) {
        gitHubRepository.deleteRepo(repo)
            .subscribe { view?.showRepoDeleted(repo) }
            .addTo(compositeDisposable)
    }

    override fun addRepo(repo: Repo) {
        gitHubRepository.addRepo(repo)
            .subscribe { view?.showRepoAdded(repo) }
            .addTo(compositeDisposable)
    }

    override fun updateRepo(repo: Repo) {
        gitHubRepository.updateRepo(repo)
            .subscribe { view?.showRepoUpdated(repo) }
            .addTo(compositeDisposable)
    }

    private fun subscribeOnRxBusEvents() {
        rxBus.observable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it) {
                    is RxEvent.DeleteRepo -> view?.showRepoDeleted(it.repo)
                    is RxEvent.UpdateRepo -> view?.showRepoUpdated(it.repo)
                    is RxEvent.FavoriteRepo -> view?.showRepoUpdated(it.repo)
                }
            }
            .addTo(compositeDisposable)
    }

}
