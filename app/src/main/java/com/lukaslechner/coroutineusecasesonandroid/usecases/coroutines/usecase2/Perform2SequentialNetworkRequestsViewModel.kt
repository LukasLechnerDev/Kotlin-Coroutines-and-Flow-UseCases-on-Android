package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<Perform2SequentialNetworkRequestsViewModel.UiState>() {

    fun perform2SequentialNetworkRequest() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            try {
                val recentVersions = getRecentAndroidVersions()
                val mostRecentVersion = recentVersions.last()

                val featuresOfMostRecentVersion =
                    getAndroidVersionFeatures(mostRecentVersion.apiVersion)

                uiState.value = UiState.Success(featuresOfMostRecentVersion)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    private suspend fun getRecentAndroidVersions() = withContext(ioDispatcher) {
        mockApi.getRecentAndroidVersions()
    }

    private suspend fun getAndroidVersionFeatures(apiVersion: Int) = withContext(ioDispatcher) {
        mockApi.getAndroidVersionFeatures(apiVersion)
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(
            val versionFeatures: VersionFeatures
        ) : UiState()

        data class Error(val message: String) : UiState()
    }
}