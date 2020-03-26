package com.lukaslechner.coroutineusecasesonandroid.fakes

import com.lukaslechner.coroutineusecasesonandroid.mock.*
import kotlinx.coroutines.CompletableDeferred

class FakeApi : MockApi {

    private var recentAndroidVersionsCompletable = CompletableDeferred<List<AndroidVersion>>()

    override suspend fun getRecentAndroidVersions() = recentAndroidVersionsCompletable.await()

    fun sendResponseToGetRecentAndroidVersionsRequest(result: List<AndroidVersion>) {
        recentAndroidVersionsCompletable.complete(result)
        recentAndroidVersionsCompletable = CompletableDeferred()
    }

    fun sendErrorToGetRecentAndroidVersionsRequest(throwable: Throwable) {
        recentAndroidVersionsCompletable.completeExceptionally(throwable)
        recentAndroidVersionsCompletable = CompletableDeferred()
    }

    override suspend fun getAndroidVersionFeatures(apiVersion: Int): VersionFeatures {
        return when (apiVersion) {
            27 -> mockVersionFeaturesOreo
            28 -> mockVersionFeaturesPie
            29 -> mockVersionFeaturesAndroid10
            else -> throw IllegalArgumentException("Unknown apiVersion")
        }
    }
}