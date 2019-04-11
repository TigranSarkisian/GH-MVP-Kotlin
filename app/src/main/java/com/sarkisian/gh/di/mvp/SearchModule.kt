package com.sarkisian.gh.di.mvp

import com.sarkisian.gh.di.scope.ActivityScoped
import com.sarkisian.gh.di.scope.FragmentScoped
import com.sarkisian.gh.ui.search.RepoSearchContract
import com.sarkisian.gh.ui.search.RepoSearchFragment
import com.sarkisian.gh.ui.search.RepoSearchPresenter
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import kotlin.text.Typography.dagger

@Module
abstract class SearchModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun bindRepoSearchFragment(): RepoSearchFragment

    @ActivityScoped
    @Binds
    abstract fun repoSearchPresenter(repoSearchPresenter: RepoSearchPresenter):
            RepoSearchContract.RepoSearchPresenter

}
