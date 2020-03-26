package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomAndCoroutinesViewModel(
    private val mockApi: MockApi = mockApi(),
    var database: AndroidVersionDao? = null,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    fun performSingleNetworkRequest() {
        viewModelScope.launch {
            uiState.value = UiState.Loading.LoadFromDb

            val localVersions = database!!.getAndroidVersions()
            if (localVersions.isNotEmpty()) {
                uiState.value =
                    UiState.Success(DataSource.Database, localVersions.mapToUiModelList())
            } else {
                uiState.value =
                    UiState.Error(DataSource.Database, "Database empty!")
            }

            uiState.value = UiState.Loading.LoadFromNetwork
            try {
                val recentVersions = getRecentAndroidVersions()
                for (recentVersion in recentVersions) {
                    database!!.insert(recentVersion.mapToEntity())
                }
                uiState.value = UiState.Success(DataSource.Network, recentVersions)
            } catch (exception: Exception) {
                uiState.value = UiState.Error(DataSource.Network, "Network Request failed")
            }
        }
    }

    private suspend fun getRecentAndroidVersions() = withContext(ioDispatcher) {
        mockApi.getRecentAndroidVersions()
    }

    fun clearDatabase() {
        viewModelScope.launch {
            database!!.clear()
        }
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
                        5000
                    )
            )
    }

    sealed class UiState {
        sealed class Loading : UiState() {
            object LoadFromDb : Loading()
            object LoadFromNetwork : Loading()
        }

        data class Success(val dataSource: DataSource, val recentVersions: List<AndroidVersion>) :
            UiState()

        data class Error(val dataSource: DataSource, val message: String) : UiState()
    }
}

sealed class DataSource(val name: String) {
    object Database : DataSource("Database")
    object Network : DataSource("Network")
}