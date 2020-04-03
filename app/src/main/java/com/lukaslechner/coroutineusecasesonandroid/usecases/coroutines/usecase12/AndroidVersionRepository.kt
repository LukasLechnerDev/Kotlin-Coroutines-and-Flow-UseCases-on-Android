package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12

import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.createMockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.*
import timber.log.Timber

class AndroidVersionRepository(
    private var database: AndroidVersionDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher),
    private val mockApi: MockApi = mockApi()
) {

    suspend fun getLocalAndroidVersions(): List<AndroidVersion> {
        return database.getAndroidVersions().mapToUiModelList()
    }

    suspend fun loadRemoteAndroidVersions(): List<AndroidVersion> {
        return scope.async {
            val recentVersions = getRecentAndroidVersions()
            for (recentVersion in recentVersions) {
                Timber.d("Insert $recentVersion to database")
                database.insert(recentVersion.mapToEntity())
            }
            recentVersions
        }.await()
    }

    private suspend fun getRecentAndroidVersions() = mockApi.getRecentAndroidVersions()

    fun clearDatabase() {
        scope.launch {
            database.clear()
        }
    }

    companion object {
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
}