package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.createMockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import com.lukaslechner.coroutineusecasesonandroid.utils.addCoroutineDebugInfo
import kotlinx.coroutines.*
import timber.log.Timber

class DebuggingCoroutinesViewModel(
    private val mockApi: MockApi = mockApi(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    fun performSingleNetworkRequest() {

        // This property needs to be set so that the coroutine name is printed when logging Thread.currentName()
        // System.setProperty("kotlinx.coroutines.debug", if (BuildConfig.DEBUG) "on" else "off")
        // It is set in [CoroutineUsecasesOnAndroidApplication]

        viewModelScope.launch(CoroutineName("Initial Coroutine")) {
            Timber.d(addCoroutineDebugInfo("Initial coroutine launched"))
            uiState.value = UiState.Loading
            try {
                val recentVersions = getRecentAndroidVersions()
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

    private suspend fun getRecentAndroidVersions() = withContext(ioDispatcher) {
        Timber.d(addCoroutineDebugInfo(("Loading recent Android Versions")))
        mockApi.getRecentAndroidVersions()
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

    fun uiState(): LiveData<UiState> = uiState
    private val uiState: MutableLiveData<UiState> = MutableLiveData()

    companion object {
        fun mockApi() =
            createMockApi(
                MockNetworkInterceptor()
                    .mock(
                        "http://localhost/recent-android-versions",
                        Gson().toJson(mockAndroidVersions),
                        200,
                        1500
                    )
            )
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val recentVersions: List<AndroidVersion>) : UiState()
        data class Error(val message: String) : UiState()
    }
}