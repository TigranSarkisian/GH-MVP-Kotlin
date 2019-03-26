package com.sarkisian.gh.di

import com.sarkisian.gh.di.scope.ActivityScoped
import com.sarkisian.gh.ui.main.MainActivity
import com.sarkisian.gh.ui.repo.RepoActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(ReposModule::class, RepoFavoritesModule::class,
            SearchModule::class))
    abstract fun bindMainActivity(): MainActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = arrayOf(RepoModule::class))
    abstract fun bindRepoActivity(): RepoActivity

}
