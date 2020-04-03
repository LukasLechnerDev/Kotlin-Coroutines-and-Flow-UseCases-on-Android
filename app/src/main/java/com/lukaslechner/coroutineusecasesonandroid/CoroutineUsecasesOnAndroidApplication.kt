package com.lukaslechner.coroutineusecasesonandroid

import android.app.Application
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12.AndroidVersionDatabase
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12.AndroidVersionRepository
import timber.log.Timber

class CoroutineUsecasesOnAndroidApplication : Application() {

    val androidVersionRepository by lazy {
        val database = AndroidVersionDatabase.getInstance(applicationContext).androidVersionDao()
        AndroidVersionRepository(database)
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        // Enable Debugging for Kotlin Coroutines in debug builds
        // Prints Coroutine name when logging Thread.currentThread().name
        System.setProperty("kotlinx.coroutines.debug", if (BuildConfig.DEBUG) "on" else "off")
    }
}