package com.sarkisian.gh.di.mvp

import com.sarkisian.gh.ui.repos.RepoListContract
import com.sarkisian.gh.ui.repos.RepoListFragment
import com.sarkisian.gh.ui.repos.RepoListPresenter
import org.koin.dsl.module

object ReposModule {

    val module = module {
        factory { RepoListFragment() }
        factory { RepoListPresenter(get(), get(), get()) as RepoListContract.RepoListPresenter }
    }

}
