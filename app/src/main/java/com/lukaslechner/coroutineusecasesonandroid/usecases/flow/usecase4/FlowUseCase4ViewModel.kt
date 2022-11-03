package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class FlowUseCase4ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsFlow: Flow<UiState> = stockPriceDataSource
        .latestStockList
        .map { stockList ->
            UiState.Success(stockList) as UiState
        }
        .onCompletion {
            Timber.tag("Flow").d("Flow has completed.")
        }.stateIn(
            scope = viewModelScope,
            initialValue = UiState.Loading,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000)
        )
}