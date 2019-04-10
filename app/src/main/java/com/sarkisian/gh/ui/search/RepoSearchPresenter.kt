package com.sarkisian.gh.ui.search


import com.jakewharton.rxbinding2.InitialValueObservable
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.ui.base.mvp.BasePresenter
import com.sarkisian.gh.util.extensions.addTo
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class RepoSearchPresenter constructor(
    private val gitHubRepository: GitHubDataSource
) : BasePresenter<RepoSearchContract.RepoSearchView>(), RepoSearchContract.RepoSearchPresenter {

    override fun searchRepos(searchObservable: InitialValueObservable<CharSequence>) {
        searchObservable
            .skipInitialValue()
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(300, TimeUnit.MILLISECONDS)
            .doOnNext { Timber.i(it.toString()) }
            .toFlowable(BackpressureStrategy.LATEST)
            .flatMap { gitHubRepository.searchRepos(it.toString()).toFlowable() }
            .doOnNext { Timber.i("Search result size ${it.items.size}") }
            .map { it.items }
            .onErrorReturn { mutableListOf() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view?.showEmptyState(it.isEmpty())
                view?.onSearchResultRetrieved(it)
            }
            .addTo(compositeDisposable)
    }

}
