package com.sarkisian.gh.di

import android.preference.PreferenceManager
import com.sarkisian.gh.GitHubApp
import com.sarkisian.gh.util.error.ErrorHandler
import com.sarkisian.gh.util.rxbus.RxBus
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

object AppModule {

    val module = module {
        single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
        single { ErrorHandler(androidContext()) }
    }

}
