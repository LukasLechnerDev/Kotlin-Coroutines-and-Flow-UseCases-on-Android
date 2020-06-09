package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch

class RoomAndCoroutinesViewModel(
    private val api: MockApi,
    var database: AndroidVersionDao
) : BaseViewModel<UiState>() {

    fun loadData() {
        uiState.value = UiState.Loading.LoadFromDb

        viewModelScope.launch {

            val localVersions = database.getAndroidVersions()
            if (localVersions.isNotEmpty()) {
                uiState.value =
                    UiState.Success(DataSource.DATABASE, localVersions.mapToUiModelList())
            } else {
                uiState.value =
                    UiState.Error(DataSource.DATABASE, "Database empty!")
            }

            uiState.value = UiState.Loading.LoadFromNetwork
            try {
                val recentVersions = api.getRecentAndroidVersions()
                for (recentVersion in recentVersions) {
                    database.insert(recentVersion.mapToEntity())
                }
                uiState.value = UiState.Success(DataSource.NETWORK, recentVersions)
            } catch (exception: Exception) {
                uiState.value = UiState.Error(DataSource.NETWORK, "Network Request failed")
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch {
            database.clear()
        }
    }
}

enum class DataSource(val dataSourceName: String) {
    DATABASE("Database"),
    NETWORK("Network")
}