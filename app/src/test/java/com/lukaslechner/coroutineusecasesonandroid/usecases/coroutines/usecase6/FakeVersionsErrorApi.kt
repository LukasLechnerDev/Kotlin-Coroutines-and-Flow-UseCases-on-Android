package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.utils.EndpointShouldNotBeCalledException
import kotlinx.coroutines.delay
import java.io.IOException

class FakeVersionsErrorApi(private val responseDelay: Long) : MockApi {

    var requestCount = 0

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        requestCount++
        delay(responseDelay)
        throw IOException()
    }

    override suspend fun getAndroidVersionFeatures(apiVersion: Int): VersionFeatures {
        throw EndpointShouldNotBeCalledException()
    }
}