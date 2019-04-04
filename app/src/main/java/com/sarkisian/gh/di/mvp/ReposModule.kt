package com.sarkisian.gh.di.mvp

import com.sarkisian.gh.di.scope.ActivityScoped
import com.sarkisian.gh.di.scope.FragmentScoped
import com.sarkisian.gh.ui.repos.RepoListContract
import com.sarkisian.gh.ui.repos.RepoListFragment
import com.sarkisian.gh.ui.repos.RepoListPresenter
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReposModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun bindRepoListFragment(): RepoListFragment

    @ActivityScoped
    @Binds
    abstract fun repoListPresenter(repoListPresenter: RepoListPresenter):
            RepoListContract.RepoListPresenter

}
