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

            withContext(Dispatchers.IO) {
                try {
                    val recentVersions = mockApi.getRecentAndroidVersions()

                    val versionFeatures = recentVersions.map { androidVersion ->
                        mockApi.getAndroidVersionFeatures(androidVersion.apiVersion)
                    }

                    withContext(Dispatchers.Main) {
                        uiState.value = UiState.Success(versionFeatures)
                    }

                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        uiState.value = UiState.Error("Network Request failed")
                    }
                }
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            withContext(Dispatchers.IO) {
                try {
                    val recentVersions = mockApi.getRecentAndroidVersions()
                    val versionFeatures = recentVersions
                        .map { androidVersion ->
                            async { mockApi.getAndroidVersionFeatures(androidVersion.apiVersion) }
                        }.awaitAll()

                    withContext(Dispatchers.Main) {
                        uiState.value = UiState.Success(versionFeatures)
                    }

                } catch (exception: Exception) {
                    withContext(Dispatchers.Main) {
                        uiState.value = UiState.Error("Network Request failed")
                    }
                }
            }
        }
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