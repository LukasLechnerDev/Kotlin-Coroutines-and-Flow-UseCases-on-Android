package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.*
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.*

class VariableAmountOfNetworkRequestsConcurrentlyViewModel : ViewModel() {

    private val mockApi = createMockApi(
        MockNetworkInterceptor()
            .mock(
                "http://localhost/recent-android-versions",
                Gson().toJson(mockAndroidVersions),
                200,
                1000
            )
            .mock(
                "http://localhost/android-version-features/27",
                Gson().toJson(mockVersionFeaturesOreo),
                200,
                1000
            )
            .mock(
                "http://localhost/android-version-features/28",
                Gson().toJson(mockVersionFeaturesPie),
                200,
                1000
            )
            .mock(
                "http://localhost/android-version-features/29",
                Gson().toJson(mockVersionFeaturesAndroid10),
                200,
                1000
            )
    )

    fun performNetworkRequestsSequentially() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                val recentVersions = getRecentAndroidVersions()
                val versionFeatures = recentVersions.map { androidVersion ->
                    getAndroidVersionFeatures(androidVersion.apiVersion)
                }
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
                val recentVersions = getRecentAndroidVersions()
                val versionFeatures = recentVersions
                    .map { androidVersion ->
                        async { getAndroidVersionFeatures(androidVersion.apiVersion) }
                    }.awaitAll()
                uiState.value = UiState.Success(versionFeatures)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
    }

    private suspend fun getRecentAndroidVersions() = withContext(Dispatchers.IO) {
        mockApi.getRecentAndroidVersions()
    }

    private suspend fun getAndroidVersionFeatures(apiVersion: Int) = withContext(Dispatchers.IO) {
        mockApi.getAndroidVersionFeatures(apiVersion)
    }

    fun uiState(): LiveData<UiState> = uiState
    private val uiState: MutableLiveData<UiState> = MutableLiveData()

    sealed class UiState {
        object Loading : UiState()
        data class Success(
            val versionFeatures: List<VersionFeatures>
        ) : UiState()

        data class Error(val message: String) : UiState()
    }
}