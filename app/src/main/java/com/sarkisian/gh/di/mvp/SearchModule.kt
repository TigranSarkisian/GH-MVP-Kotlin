package com.sarkisian.gh.di.mvp

import com.sarkisian.gh.ui.search.RepoSearchContract
import com.sarkisian.gh.ui.search.RepoSearchPresenter
import org.koin.dsl.module

object SearchModule {

    val module = module {
        factory { RepoSearchPresenter(get()) as RepoSearchContract.RepoSearchPresenter }
    }

}
