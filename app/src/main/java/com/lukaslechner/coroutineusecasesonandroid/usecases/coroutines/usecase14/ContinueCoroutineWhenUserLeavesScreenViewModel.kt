package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.launch

class ContinueCoroutineWhenUserLeavesScreenViewModel(
    private var repository: AndroidVersionRepository
) : BaseViewModel<UiState>() {

    // more information in this blogpost about "Coroutines & Patterns for work that shouldn't
    // be cancelled" =>
    // https://medium.com/androiddevelopers/coroutines-patterns-for-work-that-shouldnt-be-cancelled-e26c40f142ad

    fun loadData() {
        uiState.value = UiState.Loading.LoadFromDb

        viewModelScope.launch {
            val localVersions = repository.getLocalAndroidVersions()
            if (localVersions.isNotEmpty()) {
                uiState.value =
                    UiState.Success(DataSource.Database, localVersions)
            } else {
                uiState.value =
                    UiState.Error(DataSource.Database, "Database empty!")
            }

            uiState.value = UiState.Loading.LoadFromNetwork

            try {
                uiState.value = UiState.Success(
                    DataSource.Network,
                    repository.loadAndStoreRemoteAndroidVersions()
                )
            } catch (exception: Exception) {
                uiState.value = UiState.Error(DataSource.Network, "Network Request failed")
            }
        }
    }

    fun clearDatabase() {
        repository.clearDatabase()
    }
}

sealed class DataSource(val name: String) {
    object Database : DataSource("Database")
    object Network : DataSource("Network")
}