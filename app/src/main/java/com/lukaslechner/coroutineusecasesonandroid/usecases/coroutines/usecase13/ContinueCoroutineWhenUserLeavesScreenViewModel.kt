package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import kotlinx.coroutines.launch

class ContinueCoroutineWhenUserLeavesScreenViewModel(
    private var repository: AndroidVersionRepository
) : ViewModel() {

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

    fun uiState(): LiveData<UiState> = uiState

    private val uiState: MutableLiveData<UiState> = MutableLiveData()

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