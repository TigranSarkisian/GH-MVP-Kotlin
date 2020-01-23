package com.sarkisian.gh.di.data

import com.sarkisian.gh.data.api.ApiFactory
import com.sarkisian.gh.data.api.GitHubAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class APIModule {

    @Singleton
    @Provides
    fun provideGitHubAPI(): GitHubAPI {
        return ApiFactory.gitHubAPI
    }

}
