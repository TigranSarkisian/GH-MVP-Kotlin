package com.sarkisian.gh

import android.app.Activity
import android.app.Application
import android.os.StrictMode
import com.sarkisian.gh.data.db.RealmFactory
import com.sarkisian.gh.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject


class GitHubApp : Application(), HasActivityInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity>? = androidInjector

    override fun onCreate() {
        super.onCreate()
        installLeakCanary()
        turnOnStrictMode()
        Timber.plant(Timber.DebugTree())
        RealmFactory.init(this)
        RxJavaPlugins.setErrorHandler { throwable -> Timber.e(throwable.toString()) }
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    private fun installLeakCanary() {
        if (BuildConfig.DEBUG) {
            if (LeakCanary.isInAnalyzerProcess(this)) return
            LeakCanary.install(this)
        }
    }

    private fun turnOnStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build())
        }
    }

}
