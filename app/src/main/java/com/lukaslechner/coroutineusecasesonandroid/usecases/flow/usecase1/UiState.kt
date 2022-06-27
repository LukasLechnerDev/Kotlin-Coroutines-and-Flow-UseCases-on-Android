package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.GoogleStock

sealed class UiState {
    object Loading : UiState()
    data class Success(val googleStock: GoogleStock) : UiState()
    data class Error(val message: String) : UiState()
}