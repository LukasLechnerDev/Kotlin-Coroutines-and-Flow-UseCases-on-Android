package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.BitcoinPrice

sealed class UiState {
    object Loading : UiState()
    data class Success(val bitcoinPrice: BitcoinPrice) : UiState()
    data class Error(val message: String) : UiState()
}