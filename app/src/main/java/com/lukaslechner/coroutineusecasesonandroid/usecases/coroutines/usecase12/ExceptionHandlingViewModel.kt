package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun handleExceptionWithTryCatch() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            try {
                api.getAndroidVersionFeatures(27)

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
            api.getAndroidVersionFeatures(27)
        }
    }

    fun showResultsEvenIfChildCoroutineFails() {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            supervisorScope {
                val oreoFeaturesDeferred = async { api.getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { api.getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { api.getAndroidVersionFeatures(29) }

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
                val oreoFeaturesDeferred = async { api.getAndroidVersionFeatures(27) }
                val pieFeaturesDeferred = async { api.getAndroidVersionFeatures(28) }
                val android10FeaturesDeferred = async { api.getAndroidVersionFeatures(29) }

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
}