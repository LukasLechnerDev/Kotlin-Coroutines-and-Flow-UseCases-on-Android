package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.*
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Perform2SequentialNetworkRequestsViewModel(
    val mockApi: MockApi = mockApi(),
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    // loads all recent Android versions
    // then loads all new features of the most recent Android version
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

    fun uiState(): LiveData<UiState> = uiState
    private val uiState: MutableLiveData<UiState> = MutableLiveData()

    companion object {
        fun mockApi() = createMockApi(
            MockNetworkInterceptor()
                .mock(
                    "http://localhost/recent-android-versions",
                    Gson().toJson(mockAndroidVersions),
                    200,
                    1500
                )
                .mock(
                    "http://localhost/android-version-features/29",
                    Gson().toJson(mockVersionFeaturesAndroid10),
                    200,
                    1500
                )
        )
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(
            val versionFeatures: VersionFeatures
        ) : UiState()

        data class Error(val message: String) : UiState()
    }
}