package com.sarkisian.gh.di

import com.sarkisian.gh.GitHubApp
import com.sarkisian.gh.di.data.GitHubAPIModule
import com.sarkisian.gh.di.data.GitHubRepositoryModule
import com.sarkisian.gh.di.mvp.BuildersModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        BuildersModule::class,
        AppModule::class,
        GitHubAPIModule::class,
        GitHubRepositoryModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance

        fun application(application: GitHubApp): Builder

        fun build(): AppComponent
    }

    fun inject(application: GitHubApp)

}
