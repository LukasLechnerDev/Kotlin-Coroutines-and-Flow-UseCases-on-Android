package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase15

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lukaslechner.coroutineusecasesonandroid.mock.createMockAnalyticsApi
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import timber.log.Timber

class AnalyticsWorker(appContext: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(appContext, workerParameters) {

    private val analyticsApi = createMockAnalyticsApi()

    override suspend fun doWork(): Result {
        return try {
            analyticsApi.trackScreenOpened()
            Timber.d("Successfully tracked screen open event!")
            Result.success()
        } catch (exception: Exception) {
            Timber.e("Tracking screen open event failed!")
            Result.failure()
        }
    }

    companion object {
        fun createMockAnalyticsApi() = createMockAnalyticsApi(
            MockNetworkInterceptor()
                .mock(
                    "http://localhost/analytics/workmanager-screen-opened",
                    "true",
                    200,
                    1500
                )
        )
    }
}