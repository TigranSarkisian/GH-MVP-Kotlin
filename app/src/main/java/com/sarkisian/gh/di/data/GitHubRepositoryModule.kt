package com.sarkisian.gh.di.data

import com.sarkisian.gh.data.api.GitHubAPI
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.data.repository.GitHubLocalDataSource
import com.sarkisian.gh.data.repository.GitHubRemoteDataSource
import com.sarkisian.gh.data.repository.GitHubRepository
import com.sarkisian.gh.util.rxbus.RxBus
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class GitHubRepositoryModule {

    @Singleton
    @Provides
    fun provideRepoListRepository(
            gitHubLocalDataSource: GitHubLocalDataSource,
            gitHubRemoteDataSource: GitHubRemoteDataSource,
            rxBus: RxBus
    ): GitHubDataSource {
        return GitHubRepository(gitHubLocalDataSource, gitHubRemoteDataSource, rxBus)
    }

    @Singleton
    @Provides
    fun provideRepoListRemoteDataSource(gitHubAPI: GitHubAPI): GitHubRemoteDataSource {
        return GitHubRemoteDataSource(gitHubAPI)
    }

    @Singleton
    @Provides
    fun provideRepoListLocalDataSource(): GitHubLocalDataSource {
        return GitHubLocalDataSource()
    }

}