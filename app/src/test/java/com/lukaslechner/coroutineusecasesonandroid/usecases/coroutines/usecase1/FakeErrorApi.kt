package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.utils.EndpointShouldNotBeCalledException
import java.io.IOException

class FakeErrorApi() : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        throw IOException()
    }

    override suspend fun getAndroidVersionFeatures(apiVersion: Int): VersionFeatures {
        throw EndpointShouldNotBeCalledException()
    }
}