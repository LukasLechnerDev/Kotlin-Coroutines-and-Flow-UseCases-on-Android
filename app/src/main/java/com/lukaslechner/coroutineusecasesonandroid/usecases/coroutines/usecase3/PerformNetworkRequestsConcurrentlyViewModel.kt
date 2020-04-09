package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.*

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<PerformNetworkRequestsConcurrentlyViewModel.UiState>() {

    fun performNetworkRequestsSequentially() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                val oreoFeatures = getAndroidVersionFeatures(27)
                val pieFeatures = getAndroidVersionFeatures(28)
                val android10Features = getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            try {
                coroutineScope {
                    val oreoFeaturesDeferred = async { getAndroidVersionFeatures(27) }
                    val pieFeaturesDeferred = async { getAndroidVersionFeatures(28) }
                    val android10FeaturesDeferred = async { getAndroidVersionFeatures(29) }

                    val oreoFeatures = oreoFeaturesDeferred.await()
                    val pieFeatures = pieFeaturesDeferred.await()
                    val android10Features = android10FeaturesDeferred.await()

                    val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)

                    // other alternative: (but slightly different behavior when a deferred fails, see docs)
                    // val versionFeatures = awaitAll(oreoFeaturesDeferred, pieFeaturesDeferred, android10FeaturesDeferred)

                    uiState.value = UiState.Success(versionFeatures)
                }

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    private suspend fun getAndroidVersionFeatures(apiVersion: Int) = withContext(Dispatchers.IO) {
        mockApi.getAndroidVersionFeatures(apiVersion)
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(
            val versionFeatures: List<VersionFeatures>
        ) : UiState()

        data class Error(val message: String) : UiState()
    }
}