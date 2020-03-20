package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mockdata.*
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MockApi {

    @GET("recent-android-versions")
    suspend fun getRecentAndroidVersions(): List<AndroidVersion>

    @GET("android-version-features/{apiVersion}")
    suspend fun getAndroidVersionFeatures(@Path("apiVersion") apiVersion: Int): VersionFeatures
}

val mockApi: MockApi by lazy {
    val gson = Gson()
    val mockNetworkInterceptor = MockNetworkInterceptor()

    mockNetworkInterceptor.mock(
        "http://localhost/recent-android-versions",
        gson.toJson(mockAndroidVersions),
        200,
        1000
    )

    mockNetworkInterceptor.mock(
        "http://localhost/android-version-features/27",
        gson.toJson(mockVersionFeaturesOreo),
        200,
        1000
    )

    mockNetworkInterceptor.mock(
        "http://localhost/android-version-features/28",
        gson.toJson(mockVersionFeaturesPie),
        200,
        1000
    )

    mockNetworkInterceptor.mock(
        "http://localhost/android-version-features/29",
        gson.toJson(mockVersionFeaturesAndroid10),
        200,
        1000
    )

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(mockNetworkInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://localhost/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(MockApi::class.java)
}