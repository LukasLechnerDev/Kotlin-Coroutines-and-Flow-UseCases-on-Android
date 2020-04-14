package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.utils.addCoroutineDebugInfo
import kotlinx.coroutines.*
import timber.log.Timber

class DebuggingCoroutinesViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading

        // This property needs to be set so that the coroutine name is printed when logging Thread.currentName()
        // System.setProperty("kotlinx.coroutines.debug", if (BuildConfig.DEBUG) "on" else "off")
        // It is set in [CoroutineUsecasesOnAndroidApplication]

        viewModelScope.launch(CoroutineName("Initial Coroutine")) {
            Timber.d(addCoroutineDebugInfo("Initial coroutine launched"))
            try {
                val recentVersions = api.getRecentAndroidVersions()
                Timber.d(addCoroutineDebugInfo("Recent Android Versions returned"))
                uiState.value = UiState.Success(recentVersions)
            } catch (exception: Exception) {
                Timber.d(addCoroutineDebugInfo("Loading recent Android Versions failed"))
                uiState.value = UiState.Error("Network Request failed")
            }

            // Perform two calculations in parallel
            val calculation1Deferred =
                async(CoroutineName("Calculation1")) { performCalculation1() }
            val calculation2Deferred =
                async(CoroutineName("Calculation2")) { performCalculation2() }

            Timber.d(addCoroutineDebugInfo("Result of Calculation1: ${calculation1Deferred.await()}"))
            Timber.d(addCoroutineDebugInfo("Result of Calculation2: ${calculation2Deferred.await()}"))
        }
    }

    private suspend fun performCalculation1() = withContext(Dispatchers.Default) {
        Timber.d(addCoroutineDebugInfo("Starting Calculation1"))
        delay(1000)
        Timber.d(addCoroutineDebugInfo("Calculation1 completed"))
        13
    }

    private suspend fun performCalculation2() = withContext(Dispatchers.Default) {
        Timber.d(addCoroutineDebugInfo("Starting Calculation2"))
        delay(2000)
        Timber.d(addCoroutineDebugInfo("Calculation2 completed"))
        42
    }
}