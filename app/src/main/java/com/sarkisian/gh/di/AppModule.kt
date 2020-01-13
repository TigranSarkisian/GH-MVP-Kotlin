package com.sarkisian.gh.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.sarkisian.gh.GitHubApp
import com.sarkisian.gh.util.error.ErrorHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: GitHubApp): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)

    @Provides
    @Singleton
    fun provideErrorHandler(application: GitHubApp): ErrorHandler = ErrorHandler(application)

}
