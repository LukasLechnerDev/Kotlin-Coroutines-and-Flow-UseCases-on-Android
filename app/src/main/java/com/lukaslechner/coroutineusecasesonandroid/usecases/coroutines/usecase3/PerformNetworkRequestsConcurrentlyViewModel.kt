package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.mockdata.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mockdata.VersionFeatures
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformNetworkRequestsConcurrentlyViewModel : ViewModel() {

    fun performNetworkRequestsSequentially() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            withContext(Dispatchers.IO) {
                try {
                    val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                    val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                    val android10Features = mockApi.getAndroidVersionFeatures(29)

                    val versionFeatures = listOf(
                        VersionFeatures(AndroidVersion(27, "Oreo"), oreoFeatures),
                        VersionFeatures(AndroidVersion(28, "Pie"), pieFeatures),
                        VersionFeatures(AndroidVersion(29, "Android10"), android10Features)
                    )

                    withContext(Dispatchers.Main) {
                        uiState.value =
                            UiState.Success(versionFeatures)
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
                    val oreoFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(27) }
                    val pieFeaturesDeferred = async { mockApi.getAndroidVersionFeatures(28) }
                    val android10FeaturesDeferred = async { mockApi.getAndroidVersionFeatures(29) }

                    val oreoFeatures = oreoFeaturesDeferred.await()
                    val pieFeatures = pieFeaturesDeferred.await()
                    val android10Features = android10FeaturesDeferred.await()

                    val versionFeatures = listOf(
                        VersionFeatures(AndroidVersion(27, "Oreo"), oreoFeatures),
                        VersionFeatures(AndroidVersion(28, "Pie"), pieFeatures),
                        VersionFeatures(AndroidVersion(29, "Android10"), android10Features)
                    )

                    withContext(Dispatchers.Main) {
                        uiState.value =
                            UiState.Success(versionFeatures)
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