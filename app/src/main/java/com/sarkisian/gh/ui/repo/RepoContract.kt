package com.sarkisian.gh.ui.repo


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.mvp.MvpPresenter
import com.sarkisian.gh.ui.base.mvp.MvpView

class RepoContract {

    interface RepoView : MvpView {

        fun showRepo(repo: Repo)

        fun showRepoDeleted(repo: Repo)

        fun showRepoUpdated(repo: Repo)

    }

    interface RepoPresenter : MvpPresenter<RepoView> {

        fun loadRepo(gitHubUser: String, repoName: String)

        fun deleteRepo(repo: Repo)

        fun updateRepo(repo: Repo)

        fun addRepoToFavorites(repo: Repo)

        fun deleteRepoFromFavorites(repo: Repo)

    }

}
