package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Perform2SequentialNetworkRequestsViewModel : ViewModel() {

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
                            UiState.Success(mostRecentVersion, featuresOfMostRecentVersion)
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
            val mostRecentVersion: AndroidVersion,
            val featuresOfMostRecentVersion: List<String>
        ) : UiState()

        data class Error(val message: String) : UiState()
    }
}