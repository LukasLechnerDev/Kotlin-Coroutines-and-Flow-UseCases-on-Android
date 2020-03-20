package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.mock.createMockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Perform2SequentialNetworkRequestsViewModel : ViewModel() {

    private val mockApi = createMockApi(
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

    // loads all recent Android versions
    // then loads all new features of the most recent Android version
    fun perform2SequentialNetworkRequest() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            withContext(Dispatchers.IO) {
                try {
                    val recentVersions = mockApi.getRecentAndroidVersions()

                    val mostRecentVersion = recentVersions.last()
                    val featuresOfMostRecentVersion =
                        mockApi.getAndroidVersionFeatures(mostRecentVersion.apiVersion)

                    withContext(Dispatchers.Main) {
                        uiState.value =
                            UiState.Success(featuresOfMostRecentVersion)
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
            val versionFeatures: VersionFeatures
        ) : UiState()

        data class Error(val message: String) : UiState()
    }
}