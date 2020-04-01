package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.*
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.*
import timber.log.Timber

class ExceptionHandlingViewModel : ViewModel() {

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
                MockNetworkInterceptor.INTERNAL_SERVER_ERROR_HTTP_CODE,
                100
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

    fun handleExceptionWithTryCatch() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                getAndroidVersionFeatures(27)

            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network Request failed: $exception")
            }
        }
    }

    fun handleWithCoroutineExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            uiState.value = UiState.Error("Network Request failed!! $exception")
        }

        viewModelScope.launch(exceptionHandler) {
            uiState.value = UiState.Loading
            getAndroidVersionFeatures(27)
        }
    }

    fun showResultsEvenIfChildCoroutineFails() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            supervisorScope {
                val oreoFeaturesDeferred = async { getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { getAndroidVersionFeatures(29) }

                val oreoFeatures = try {
                    oreoFeaturesDeferred.await()
                } catch (e: Exception) {
                    Timber.e("Error loading oreo features")
                    null
                }

                val pieFeatures = try {
                    pieFeaturesDeferred.await()
                } catch (e: Exception) {
                    Timber.e("Error loading pie features")
                    null
                }

                val android10Features = try {
                    android10FeaturesDeferred.await()
                } catch (e: Exception) {
                    Timber.e("Error loading oreo features")
                    null
                }
                val versionFeatures = listOfNotNull(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)
            }
        }
    }

    fun showResultsEvenIfChildCoroutineFailsRunCatching() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            supervisorScope {
                val oreoFeaturesDeferred = async { getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { getAndroidVersionFeatures(29) }

                val versionFeatures = listOf(
                    oreoFeaturesDeferred,
                    pieFeaturesDeferred,
                    android10FeaturesDeferred
                ).mapNotNull { deferred ->
                    runCatching {
                        deferred.await()
                    }.onFailure {
                        Timber.e("Failure loading features of an Android Version")
                    }.getOrNull()
                }

                uiState.value = UiState.Success(versionFeatures)
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