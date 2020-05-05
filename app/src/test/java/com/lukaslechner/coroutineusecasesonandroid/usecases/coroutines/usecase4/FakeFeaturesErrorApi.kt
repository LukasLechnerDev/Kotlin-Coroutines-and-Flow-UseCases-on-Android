package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import com.lukaslechner.coroutineusecasesonandroid.mock.*
import kotlinx.coroutines.delay
import java.io.IOException

class FakeFeaturesErrorApi(private val responseDelay: Long) : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        delay(responseDelay)
        return mockAndroidVersions
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        delay(responseDelay)
        return when (apiLevel) {
            27 -> mockVersionFeaturesOreo
            28 -> throw IOException()
            29 -> mockVersionFeaturesAndroid10
            else -> throw IllegalArgumentException("apiLevel not found")
        }
    }
}