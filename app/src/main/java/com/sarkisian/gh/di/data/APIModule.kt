package com.sarkisian.gh.di.data

import com.sarkisian.gh.data.api.ApiFactory
import com.sarkisian.gh.data.api.ApiFactory.BASE_URL
import org.koin.dsl.module

object APIModule {

    val module = module {
        single { ApiFactory.getGitHubAPI(BASE_URL) }
    }

}
