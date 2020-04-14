package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            val numberOfRetries = 2
            try {
                retryNetworkRequest(times = numberOfRetries) {
                    val recentVersions = api.getRecentAndroidVersions()
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
}