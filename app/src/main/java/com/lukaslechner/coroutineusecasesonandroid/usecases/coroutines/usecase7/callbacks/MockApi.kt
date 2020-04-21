package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7.callbacks

import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesOreo
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesPie
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

fun mockApi(): CallbackMockApi = createMockApi(
    MockNetworkInterceptor()
        // timeout on first request for oreo features
        .mock(
            "http://localhost/android-version-features/27",
            Gson().toJson(mockVersionFeaturesOreo),
            200,
            1200,
            persist = false
        )
        // network error on second request
        .mock(
            "http://localhost/android-version-features/27",
            "Something went wrong on servers side",
            500,
            300,
            persist = false
        )
        // 3rd request is successful and within timeout
        .mock(
            "http://localhost/android-version-features/27",
            Gson().toJson(mockVersionFeaturesOreo),
            200,
            100
        )
        // timeout on first request for pie features
        .mock(
            "http://localhost/android-version-features/28",
            Gson().toJson(mockVersionFeaturesPie),
            200,
            1200,
            persist = false
        )
        // network error on second request
        .mock(
            "http://localhost/android-version-features/28",
            "Something went wrong on servers side",
            500,
            200,
            persist = false
        )
        // 3rd request is successful and within timeout
        .mock(
            "http://localhost/android-version-features/28",
            Gson().toJson(mockVersionFeaturesPie),
            200,
            100
        )
)

interface CallbackMockApi {

    @GET("recent-android-versions")
    fun getRecentAndroidVersions(): Call<List<AndroidVersion>>

    @GET("android-version-features/{apiVersion}")
    fun getAndroidVersionFeatures(@Path("apiVersion") apiVersion: Int): Call<VersionFeatures>
}

fun createMockApi(interceptor: MockNetworkInterceptor): CallbackMockApi {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(CallbackMockApi::class.java)
}