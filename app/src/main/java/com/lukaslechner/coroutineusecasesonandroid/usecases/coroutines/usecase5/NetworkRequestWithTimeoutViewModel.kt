package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class NetworkRequestWithTimeoutViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest(timeOut: Long) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                withTimeout(timeOut) {
                    val recentVersions = api.getRecentAndroidVersions()
                    uiState.value = UiState.Success(recentVersions)
                }
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }
}