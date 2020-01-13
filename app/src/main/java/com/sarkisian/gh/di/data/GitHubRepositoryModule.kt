package com.sarkisian.gh.di.data

import com.sarkisian.gh.data.api.GitHubAPI
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.data.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class GitHubRepositoryModule {

    @Singleton
    @Provides
    fun provideRepoListRepository(
        gitHubAPI: GitHubAPI
    ): GitHubDataSource = GitHubRepository(gitHubAPI)

}
