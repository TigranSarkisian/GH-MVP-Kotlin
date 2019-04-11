package com.sarkisian.gh.di

import android.preference.PreferenceManager
import com.sarkisian.gh.data.api.ApiFactory
import com.sarkisian.gh.data.repository.GitHubDataSource
import com.sarkisian.gh.data.repository.GitHubRepository
import com.sarkisian.gh.ui.favorites.RepoFavoritesContract
import com.sarkisian.gh.ui.favorites.RepoFavoritesFragment
import com.sarkisian.gh.ui.favorites.RepoFavoritesPresenter
import com.sarkisian.gh.ui.repo.RepoContract
import com.sarkisian.gh.ui.repo.RepoPresenter
import com.sarkisian.gh.ui.repos.RepoListContract
import com.sarkisian.gh.ui.repos.RepoListPresenter
import com.sarkisian.gh.ui.search.RepoSearchContract
import com.sarkisian.gh.ui.search.RepoSearchPresenter
import com.sarkisian.gh.util.error.ErrorHandler
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object AppModule {

    val appModule = module {
        single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
        single { ErrorHandler(androidContext()) }
    }

    val apiModule = module {
        single { ApiFactory.getGitHubAPI(ApiFactory.BASE_URL) }
    }

    val repositoryModule = module {
        single { GitHubRepository(get()) as GitHubDataSource }
    }

    val mvpModule = module {
        factory { RepoFavoritesPresenter(get(), get()) as RepoFavoritesContract.RepoFavoritesPresenter }
        factory { RepoPresenter(get(), get()) as RepoContract.RepoPresenter }
        factory { RepoListPresenter(get(), get()) as RepoListContract.RepoListPresenter }
        factory { RepoSearchPresenter(get()) as RepoSearchContract.RepoSearchPresenter }
    }

}
