package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.utils.EndpointShouldNotBeCalledException
import java.io.IOException

class FakeVersionsErrorApi : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        throw IOException()
    }

    override suspend fun getAndroidVersionFeatures(apiVersion: Int): VersionFeatures {
        throw EndpointShouldNotBeCalledException()
    }
}