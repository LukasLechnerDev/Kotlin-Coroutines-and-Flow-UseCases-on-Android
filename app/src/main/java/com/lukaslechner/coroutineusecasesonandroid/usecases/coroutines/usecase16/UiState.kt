package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase16

sealed class UiState {
    object Loading : UiState()
    data class Success(
        val result: String,
        val computationDuration: Long,
        val stringConversionDuration: Long,
        val factorialOf: String,
        val numberOfCoroutines: String,
        val dispatcherName: String,
        val yieldDuringCalculation: Boolean
    ) : UiState()

    data class Error(val message: String) : UiState()
}