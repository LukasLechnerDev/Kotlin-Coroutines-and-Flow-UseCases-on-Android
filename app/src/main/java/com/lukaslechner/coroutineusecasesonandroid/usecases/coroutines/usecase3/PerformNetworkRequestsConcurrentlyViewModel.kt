package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.*
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformNetworkRequestsConcurrentlyViewModel : ViewModel() {

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

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed")
            }
        }
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