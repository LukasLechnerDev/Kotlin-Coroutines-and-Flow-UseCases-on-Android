package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mockdata.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mockdata.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MockApi {

    @GET("recent-android-versions")
    suspend fun getRecentAndroidVersions(): List<AndroidVersion>
}

val mockApi: MockApi by lazy {
    val gson = Gson()
    val mockNetworkInterceptor =
        MockNetworkInterceptor()

    mockNetworkInterceptor.mock(
        "http://localhost/recent-android-versions",
        gson.toJson(mockAndroidVersions),
        200,
        1500
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