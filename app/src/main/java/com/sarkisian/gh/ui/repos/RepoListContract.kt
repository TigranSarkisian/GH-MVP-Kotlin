package com.sarkisian.gh.ui.repos


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.mvp.MvpPresenter
import com.sarkisian.gh.ui.base.mvp.MvpView

class RepoListContract {

    interface RepoListView : MvpView {

        fun showRepos(repoList: MutableList<Repo>)

        fun showNextPageRepos(repoList: MutableList<Repo>)

        fun showNextPageLoadingIndicator(value: Boolean)

        fun showRepoDeleted(repo: Repo)

        fun showRepoAdded(repo: Repo)

        fun showRepoUpdated(repo: Repo)

    }

    interface RepoListPresenter : MvpPresenter<RepoListView> {

        fun loadRepos(username: String, forceUpdate: Boolean = false)

        fun loadNextPage()

        fun deleteRepo(repo: Repo)

        fun addRepo(repo: Repo)

        fun updateRepo(repo: Repo)

    }

}
