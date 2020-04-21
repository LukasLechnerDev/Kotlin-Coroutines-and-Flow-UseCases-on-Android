package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7.callbacks

import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures

sealed class UiState {
    object Loading : UiState()
    data class Success(val versionFeatures: List<VersionFeatures>) : UiState()
    data class Error(val message: String) : UiState()
}