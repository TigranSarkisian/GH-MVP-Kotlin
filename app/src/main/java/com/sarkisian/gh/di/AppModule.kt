package com.sarkisian.gh.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.sarkisian.gh.GitHubApp
import com.sarkisian.gh.data.api.ApiFactory
import com.sarkisian.gh.data.api.ApiFactory.BASE_URL
import com.sarkisian.gh.data.api.GitHubAPI
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.rxbus.RxBus
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: GitHubApp): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    fun provideErrorHandler(application: GitHubApp): ErrorHandler {
        return ErrorHandler(application)
    }

    @Provides
    @Singleton
    fun provideRxBus(): RxBus {
        return RxBus()
    }

    @Singleton
    @Provides
    fun provideGitHubAPI(): GitHubAPI {
        return ApiFactory.getGitHubAPI(BASE_URL)
    }

}
