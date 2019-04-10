package com.sarkisian.gh.di.mvp

import com.sarkisian.gh.ui.favorites.RepoFavoritesContract
import com.sarkisian.gh.ui.favorites.RepoFavoritesPresenter
import org.koin.dsl.module

object RepoFavoritesModule {

    val module = module {
        factory { RepoFavoritesPresenter(get(), get(), get()) as RepoFavoritesContract.RepoFavoritesPresenter }
    }

}
