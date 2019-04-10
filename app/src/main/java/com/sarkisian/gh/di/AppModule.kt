package com.sarkisian.gh.di

import android.preference.PreferenceManager
import com.sarkisian.gh.util.error.ErrorHandler
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object AppModule {

    val module = module {
        single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
        single { ErrorHandler(androidContext()) }
    }

}
