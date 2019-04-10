package com.sarkisian.gh.di.data

import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.data.repository.GitHubRepository
import org.koin.dsl.module

object GitHubRepositoryModule {

    val module = module {
        single { GitHubRepository(get()) as GitHubDataSource }
    }

}
