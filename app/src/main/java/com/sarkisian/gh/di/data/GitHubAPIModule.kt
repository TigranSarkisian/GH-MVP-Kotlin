package com.sarkisian.gh.di.data

import com.sarkisian.gh.data.api.ApiFactory
import com.sarkisian.gh.data.api.ApiFactory.BASE_URL
import com.sarkisian.gh.data.api.GitHubAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GitHubAPIModule {

    @Singleton
    @Provides
    fun provideGitHubAPI(): GitHubAPI {
        return ApiFactory.getGitHubAPI(BASE_URL)
    }

}
