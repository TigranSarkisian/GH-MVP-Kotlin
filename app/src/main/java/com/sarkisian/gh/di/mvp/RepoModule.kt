package com.sarkisian.gh.di.mvp

import com.sarkisian.gh.ui.repo.RepoActivity
import com.sarkisian.gh.ui.repo.RepoContract
import com.sarkisian.gh.ui.repo.RepoPresenter
import org.koin.dsl.module

object RepoModule {

    val module = module {
        factory { RepoActivity() }
        factory { RepoPresenter(get(), get()) as RepoContract.RepoPresenter }
    }

}
