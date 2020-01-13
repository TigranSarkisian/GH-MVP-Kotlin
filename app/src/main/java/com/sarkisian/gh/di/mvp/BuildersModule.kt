package com.sarkisian.gh.di.mvp

import com.sarkisian.gh.di.scope.ActivityScoped
import com.sarkisian.gh.ui.main.MainActivity
import com.sarkisian.gh.ui.repo.RepoActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            ReposModule::class,
            RepoFavoritesModule::class,
            SearchModule::class
        ]
    )
    abstract fun bindMainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [RepoModule::class])
    abstract fun bindRepoActivity(): RepoActivity

}
