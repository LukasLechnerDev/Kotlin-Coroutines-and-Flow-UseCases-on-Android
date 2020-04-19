package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.usingcallbacks

import com.lukaslechner.coroutineusecasesonandroid.mock.*
import retrofit2.Call
import retrofit2.mock.Calls

class FakeSuccessApi : CallbackMockApi {

    override fun getRecentAndroidVersions(): Call<List<AndroidVersion>> {
        return Calls.response(mockAndroidVersions)
    }

    override fun getAndroidVersionFeatures(apiVersion: Int): Call<VersionFeatures> {
        val featureMocks = when (apiVersion) {
            27 -> mockVersionFeaturesOreo
            28 -> mockVersionFeaturesPie
            29 -> mockVersionFeaturesAndroid10
            else -> throw IllegalArgumentException("apiVersion not found")
        }
        return Calls.response(featureMocks)
    }
}