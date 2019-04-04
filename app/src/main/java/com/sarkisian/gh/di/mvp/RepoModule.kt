package com.sarkisian.gh.di.mvp

import com.sarkisian.gh.di.scope.ActivityScoped
import com.sarkisian.gh.ui.repo.RepoContract
import com.sarkisian.gh.ui.repo.RepoPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {

    @ActivityScoped
    @Binds
    abstract fun repoPresenter(repoPresenter: RepoPresenter): RepoContract.RepoPresenter

}
