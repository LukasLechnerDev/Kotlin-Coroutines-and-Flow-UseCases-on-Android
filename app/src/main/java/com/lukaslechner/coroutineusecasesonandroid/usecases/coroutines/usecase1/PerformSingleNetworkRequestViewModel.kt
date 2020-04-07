package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<PerformSingleNetworkRequestViewModel.UiState>() {

    fun performSingleNetworkRequest() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                val recentVersions = getRecentAndroidVersions()
                uiState.value = UiState.Success(recentVersions)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    private suspend fun getRecentAndroidVersions() = withContext(ioDispatcher) {
        mockApi.getRecentAndroidVersions()
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val recentVersions: List<AndroidVersion>) : UiState()
        data class Error(val message: String) : UiState()
    }
}