package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import com.lukaslechner.coroutineusecasesonandroid.mock.*
import java.io.IOException

class FakeFeaturesErrorApi() : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        return mockAndroidVersions
    }

    override suspend fun getAndroidVersionFeatures(apiVersion: Int): VersionFeatures {
        return when (apiVersion) {
            27 -> mockVersionFeaturesOreo
            28 -> mockVersionFeaturesPie
            29 -> throw IOException()
            else -> throw IllegalArgumentException("apiVersion not found")
        }
    }
}