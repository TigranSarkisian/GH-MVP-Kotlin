package com.sarkisian.gh.ui.repos


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.mvp.MvpPresenter
import com.sarkisian.gh.ui.base.mvp.MvpView

class RepoListContract {

    interface RepoListView : MvpView {

        fun onReposLoaded(repoList: MutableList<Repo>)

        fun onRepoDeleted(repo: Repo)

        fun onRepoAdded(repo: Repo)

        fun onRepoUpdated(repo: Repo)

    }

    interface RepoListPresenter : MvpPresenter<RepoListView> {

        fun loadRepos(username: String)

        fun deleteRepo(repo: Repo)

        fun addRepo(repo: Repo)

        fun updateRepo(repo: Repo)

    }

}
