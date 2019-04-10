package com.sarkisian.gh.di.data

import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.data.repository.GitHubLocalDataSource
import com.sarkisian.gh.data.repository.GitHubRemoteDataSource
import com.sarkisian.gh.data.repository.GitHubRepository
import org.koin.dsl.module

object GitHubRepositoryModule {

    val module = module {
        single { GitHubLocalDataSource() }
        single { GitHubRemoteDataSource(get()) }
        single { GitHubRepository(get(), get(), get()) as GitHubDataSource }
    }

}