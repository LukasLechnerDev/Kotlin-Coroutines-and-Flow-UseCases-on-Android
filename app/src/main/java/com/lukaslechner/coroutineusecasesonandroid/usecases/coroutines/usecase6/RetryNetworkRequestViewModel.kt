package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.createMockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class RetryNetworkRequestViewModel : ViewModel() {

    private val mockApi = createMockApi(
        MockNetworkInterceptor()
            .mock(
                "http://localhost/recent-android-versions",
                "something went wrong on server side",
                500,
                1000,
                persist = false
            ).mock(
                "http://localhost/recent-android-versions",
                "something went wrong on server side",
                500,
                1000,
                persist = false
            ).mock(
                "http://localhost/recent-android-versions",
                Gson().toJson(mockAndroidVersions),
                200,
                1000
            )
    )

    fun performSingleNetworkRequest() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            val numberOfRetries = 2
            try {
                retryNetworkRequest(times = numberOfRetries) {
                    val recentVersions = getRecentAndroidVersions()
                    uiState.value = UiState.Success(recentVersions)
                }
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    // retry with exponential backoff
    // inspired by https://stackoverflow.com/questions/46872242/how-to-exponential-backoff-retry-on-kotlin-coroutines
    private suspend fun <T> retryNetworkRequest(
        times: Int = 2,
        initialDelayMillis: Long = 100,
        maxDelayMillis: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis
        repeat(times) {
            try {
                return block()
            } catch (httpException: HttpException) {
                Timber.e(httpException)
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
        }
        return block() // last attempt
    }

    private suspend fun getRecentAndroidVersions() = withContext(Dispatchers.IO) {
        mockApi.getRecentAndroidVersions()
    }

    fun uiState(): LiveData<UiState> = uiState
    private val uiState: MutableLiveData<UiState> = MutableLiveData()

    sealed class UiState {
        object Loading : UiState()
        data class Success(val recentVersions: List<AndroidVersion>) : UiState()
        data class Error(val message: String) : UiState()
    }
}