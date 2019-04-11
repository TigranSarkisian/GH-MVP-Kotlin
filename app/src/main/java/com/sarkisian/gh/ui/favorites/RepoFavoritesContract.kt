package com.sarkisian.gh.ui.favorites


import com.sarkisian.gh.data.entity.Repo
import com.sarkisian.gh.ui.base.mvp.MvpPresenter
import com.sarkisian.gh.ui.base.mvp.MvpView

class RepoFavoritesContract {

    interface RepoFavoritesView : MvpView {

        fun onFavoriteReposLoaded(repoList: MutableList<Repo>)

        fun onRepoDeletedFromFavorites(repo: Repo)

        fun onRepoAddedToFavorites(repo: Repo)

        fun onRepoUpdated(repo: Repo)

    }

    interface RepoFavoritesPresenter : MvpPresenter<RepoFavoritesView> {

        fun getFavoriteRepos()

        fun deleteRepoFromFavorites(repo: Repo)

    }

}
