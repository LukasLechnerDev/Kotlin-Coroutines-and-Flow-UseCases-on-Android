package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.launch

class FlowUseCase1ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsLiveData: MutableLiveData<UiState> = MutableLiveData()

    init {
        viewModelScope.launch {
            stockPriceDataSource.latestStockList.collect { stockList ->
                currentStockPriceAsLiveData.value = UiState.Success(stockList)
            }
        }
    }
}