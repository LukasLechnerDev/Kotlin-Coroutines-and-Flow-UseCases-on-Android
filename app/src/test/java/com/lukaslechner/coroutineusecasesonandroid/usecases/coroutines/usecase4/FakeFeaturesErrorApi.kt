package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import com.lukaslechner.coroutineusecasesonandroid.mock.*
import kotlinx.coroutines.delay
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FakeFeaturesErrorApi(private val responseDelay: Long) : MockApi {

    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        delay(responseDelay)
        return mockAndroidVersions
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        delay(responseDelay)
        return when (apiLevel) {
            27 -> mockVersionFeaturesOreo
            28 -> throw HttpException(
                Response.error<List<AndroidVersion>>(
                    500,
                    ResponseBody.create(MediaType.parse("application/json"), "")
                )
            )
            29 -> mockVersionFeaturesAndroid10
            else -> throw IllegalArgumentException("apiLevel not found")
        }
    }
}