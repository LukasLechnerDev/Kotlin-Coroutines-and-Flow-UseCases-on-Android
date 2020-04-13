package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import com.lukaslechner.coroutineusecasesonandroid.mock.*
import kotlinx.coroutines.delay

class FakeSuccessApi(private val responseDelay: Long) : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        TODO("Not yet implemented")
    }

    override suspend fun getAndroidVersionFeatures(apiVersion: Int): VersionFeatures {
        delay(responseDelay)
        return when (apiVersion) {
            27 -> mockVersionFeaturesOreo
            28 -> mockVersionFeaturesPie
            29 -> mockVersionFeaturesAndroid10
            else -> throw IllegalArgumentException("apiVersion not found")
        }
    }
}