package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkManagerViewModel(private val context: Context) : ViewModel() {

    fun performAnalyticsRequest() {
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val request = OneTimeWorkRequestBuilder<AnalyticsWorker>()
            .setConstraints(constraints)
            // Set a delay to not slow down other UI related requests that should run fast
            .setInitialDelay(10, TimeUnit.SECONDS)
            .addTag("analyitcs-work-request")
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}