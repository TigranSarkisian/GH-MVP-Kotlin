package com.sarkisian.gh.di

import com.sarkisian.gh.GitHubApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        BuildersModule::class,
        AppModule::class,
        GitHubRepositoryModule::class))
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance

        fun application(application: GitHubApp): Builder

        fun build(): AppComponent
    }

    fun inject(application: GitHubApp)

}
