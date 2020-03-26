package com.lukaslechner.coroutineusecasesonandroid

import android.app.Application
import timber.log.Timber

class CoroutineUsecasesOnAndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}