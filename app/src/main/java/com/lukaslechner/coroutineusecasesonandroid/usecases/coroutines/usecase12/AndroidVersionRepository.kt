package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12

import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.createMockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.*

object AndroidVersionRepository {

    private var database: AndroidVersionDao? = null
    private val scope = CoroutineScope(SupervisorJob())
    private val ioDispatcher = Dispatchers.IO
    private val mockApi = mockApi()

    suspend fun getLocalAndroidVersions(): List<AndroidVersion> {
        return database!!.getAndroidVersions().mapToUiModelList()
    }

    suspend fun loadRecentAndroidVersions(): List<AndroidVersion> {
        return scope.async {
            val recentVersions = getRecentAndroidVersions()
            for (recentVersion in recentVersions) {
                database!!.insert(recentVersion.mapToEntity())
            }
            recentVersions
        }.await()
    }

    private suspend fun getRecentAndroidVersions() = withContext(ioDispatcher) {
        mockApi.getRecentAndroidVersions()
    }

    fun mockApi() =
        createMockApi(
            MockNetworkInterceptor()
                .mock(
                    "http://localhost/recent-android-versions",
                    Gson().toJson(mockAndroidVersions),
                    200,
                    5000
                )
        )
}