package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class FlowUseCase1ViewModel(
    stockPriceDataSource: StockPriceDataSource
) : BaseViewModel<UiState>() {

    val currentStockPriceAsLiveData: LiveData<UiState> = MutableLiveData()

    init {
        viewModelScope.launch {
            stockPriceDataSource.latestStockList.collect { stockList ->
                Timber.d("Received item: ${stockList.first()}")
            }
        }
    }
}