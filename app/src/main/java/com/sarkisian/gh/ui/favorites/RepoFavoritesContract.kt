package com.sarkisian.gh.ui.favorites


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.mvp.MvpPresenter
import com.sarkisian.gh.ui.base.mvp.MvpView

class RepoFavoritesContract {

    interface RepoFavoritesView : MvpView {

        fun showFavoriteRepos(repoList: MutableList<Repo>)

        fun showRepoDeletedFromFavorites(repo: Repo)

        fun showRepoAddedToFavorites(repo: Repo)

        fun showRepoUpdated(repo: Repo)

    }

    interface RepoFavoritesPresenter : MvpPresenter<RepoFavoritesView> {

        fun loadFavoriteRepos(username: String)

        fun deleteRepoFromFavorites(repo: Repo)

    }

}
