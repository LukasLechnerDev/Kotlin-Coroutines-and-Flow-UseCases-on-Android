package com.lukaslechner.coroutineusecasesonandroid.fakes

import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.CompletableDeferred

class FakeApi : MockApi {

    private var recentAndroidVersionsCompletable = CompletableDeferred<List<AndroidVersion>>()

    private val versionFeaturesCompletables = mapOf(
        27 to CompletableDeferred<VersionFeatures>(),
        28 to CompletableDeferred<VersionFeatures>(),
        29 to CompletableDeferred<VersionFeatures>()
    )

    override suspend fun getRecentAndroidVersions() = recentAndroidVersionsCompletable.await()

    fun sendResponseToGetRecentAndroidVersionsRequest(result: List<AndroidVersion>) {
        recentAndroidVersionsCompletable.complete(result)
        recentAndroidVersionsCompletable = CompletableDeferred()
    }

    fun sendErrorToGetRecentAndroidVersionsRequest(throwable: Throwable) {
        recentAndroidVersionsCompletable.completeExceptionally(throwable)
        recentAndroidVersionsCompletable = CompletableDeferred()
    }

    fun sendResponseToGetAndroidVersionFeaturesRequest(
        apiVersion: Int,
        versionFeatures: VersionFeatures
    ) {
        versionFeaturesCompletables[apiVersion]?.complete(versionFeatures)
            ?: throw IllegalArgumentException(
                "apiVersion not found"
            )
    }

    fun sendErrorToGetAndroidVersionFeaturesRequest(apiVersion: Int, throwable: Throwable) {
        versionFeaturesCompletables[apiVersion]?.completeExceptionally(throwable)
            ?: throw IllegalArgumentException(
                "apiVersion not found"
            )
    }

    override suspend fun getAndroidVersionFeatures(apiVersion: Int): VersionFeatures {
        return versionFeaturesCompletables[apiVersion]?.await()
            ?: throw IllegalArgumentException("apiVersion not found")
    }
}