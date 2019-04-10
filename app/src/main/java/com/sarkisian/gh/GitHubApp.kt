package com.sarkisian.gh

import android.app.Application
import android.os.StrictMode
import com.sarkisian.gh.data.db.RealmFactory
import com.sarkisian.gh.di.AppModule
import com.sarkisian.gh.di.data.APIModule
import com.sarkisian.gh.di.data.GitHubRepositoryModule
import com.sarkisian.gh.di.mvp.RepoFavoritesModule
import com.sarkisian.gh.di.mvp.RepoModule
import com.sarkisian.gh.di.mvp.ReposModule
import com.sarkisian.gh.di.mvp.SearchModule
import com.squareup.leakcanary.LeakCanary
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber


class GitHubApp : Application() {

    override fun onCreate() {
        super.onCreate()
        installLeakCanary()
        turnOnStrictMode()
        Timber.plant(Timber.DebugTree())
        RealmFactory.init(this)
        RxJavaPlugins.setErrorHandler { throwable -> Timber.e(throwable.toString()) }

        startKoin {
            androidLogger()
            androidContext(this@GitHubApp)
            modules(
                AppModule.module,
                GitHubRepositoryModule.module,
                APIModule.module,
                ReposModule.module,
                RepoFavoritesModule.module,
                RepoModule.module,
                SearchModule.module
            )
        }
    }

    private fun installLeakCanary() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) return
            LeakCanary.install(this)
        }
    }

    private fun turnOnStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }
    }

}
