package com.sarkisian.gh.ui.search


import com.jakewharton.rxbinding2.InitialValueObservable
import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.mvp.MvpPresenter
import com.sarkisian.gh.ui.base.mvp.MvpView

class RepoSearchContract {

    interface RepoSearchView : MvpView {

        fun onSearchResultRetrieved(repoList: MutableList<Repo>)

    }

    interface RepoSearchPresenter : MvpPresenter<RepoSearchView> {

        fun searchRepos(searchObservable: InitialValueObservable<CharSequence>)

    }

}
