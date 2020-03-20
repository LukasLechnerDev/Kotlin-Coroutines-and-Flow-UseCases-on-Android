package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.createMockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerformSingleNetworkRequestViewModel : ViewModel() {

    private val mockApi = createMockApi(
        MockNetworkInterceptor()
            .mock(
                "http://localhost/recent-android-versions",
                Gson().toJson(mockAndroidVersions),
                200,
                1500
            )
    )

    fun performSingleNetworkRequest() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            withContext(Dispatchers.IO) {
                try {
                    val recentVersions = mockApi.getRecentAndroidVersions()
                    withContext(Dispatchers.Main) {
                        uiState.value = UiState.Success(recentVersions)
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
        data class Success(val recentVersions: List<AndroidVersion>) : UiState()
        data class Error(val message: String) : UiState()
    }
}