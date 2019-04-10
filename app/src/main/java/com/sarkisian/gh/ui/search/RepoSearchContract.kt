package com.sarkisian.gh.ui.search


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.mvp.MvpPresenter
import com.sarkisian.gh.ui.base.mvp.MvpView

class RepoSearchContract {

    interface RepoSearchView : MvpView {

        fun showSearchResult(repoList: MutableList<Repo>)

        fun showSearchCleared()

        fun showNextPageRepos(repoList: MutableList<Repo>)

        fun showNextPageLoadingIndicator(value: Boolean)
    }

    interface RepoSearchPresenter : MvpPresenter<RepoSearchView> {

        fun processInput(query: String)

        fun searchRepos(query: String)

        fun loadNextPage()

        fun clearSearch()
    }

}
