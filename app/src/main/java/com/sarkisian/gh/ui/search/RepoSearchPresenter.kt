package com.sarkisian.gh.ui.search


import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.ui.base.mvp.BasePresenter
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.extensions.addTo
import com.sarkisian.gh.util.extensions.emptyString
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RepoSearchPresenter @Inject constructor(
    private val gitHubRepository: GitHubDataSource,
    private val errorHandler: ErrorHandler
) : BasePresenter<RepoSearchContract.RepoSearchView>(), RepoSearchContract.RepoSearchPresenter {

    private var loadingNextPage = false
    private var currentPage = 1
    private var subject: PublishSubject<String>? = PublishSubject.create<String>()
    private var query: String = emptyString()

    override fun attachView(view: RepoSearchContract.RepoSearchView) {
        super.attachView(view)
        observeSearch()
    }

    override fun processInput(query: String) {
        this.query = query
        subject?.onNext(query)
    }

    override fun searchRepos(query: String) {
        gitHubRepository.searchRepos(query, currentPage)
            .subscribe(
                {
                    view?.showSearchResult(it.items)
                    view?.showEmptyState(it.items.isEmpty())
                    currentPage = 1
                    loadingNextPage = false
                },
                { it -> errorHandler.readError(it) { view?.showMessage(it) } }
            ).addTo(compositeDisposable)
    }

    override fun loadNextPage() {
        if (!loadingNextPage) {
            currentPage++

            gitHubRepository.searchRepos(this.query, currentPage)
                .doOnSubscribe {
                    loadingNextPage = true
                    view?.showNextPageLoadingIndicator(true)
                }
                .doAfterTerminate {
                    loadingNextPage = false
                    view?.showNextPageLoadingIndicator(false)
                }
                .subscribe(
                    { view?.showNextPageRepos(it.items) },
                    { it -> errorHandler.readError(it) { view?.showMessage(it) } }
                )
                .addTo(compositeDisposable)
        }
    }

    override fun clearSearch() {
        view?.showSearchCleared()
    }

    private fun observeSearch() {
        val searchObservable = subject as Observable<String>
        searchObservable
            .filter(String::isNotBlank)
            .throttleLast(300, TimeUnit.MILLISECONDS)
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter(String::isNotBlank)
            .distinctUntilChanged()
            .subscribe {
                currentPage = 1
                loadingNextPage = false
                searchRepos(it)
            }
            .addTo(compositeDisposable)
    }

}
