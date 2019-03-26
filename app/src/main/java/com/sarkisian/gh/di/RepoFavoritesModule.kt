package com.sarkisian.gh.di

import com.sarkisian.gh.di.scope.ActivityScoped
import com.sarkisian.gh.di.scope.FragmentScoped
import com.sarkisian.gh.ui.favorites.RepoFavoritesContract
import com.sarkisian.gh.ui.favorites.RepoFavoritesFragment
import com.sarkisian.gh.ui.favorites.RepoFavoritesPresenter
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RepoFavoritesModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun bindRepoFavoritesFragment(): RepoFavoritesFragment

    @ActivityScoped
    @Binds
    abstract fun repoFavoritesPresenter(repoFavoritesPresenter: RepoFavoritesPresenter):
            RepoFavoritesContract.RepoFavoritesPresenter

}
