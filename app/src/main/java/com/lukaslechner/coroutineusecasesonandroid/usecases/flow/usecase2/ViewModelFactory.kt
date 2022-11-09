package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher

class ViewModelFactory(
    private val stockPriceDataSource: StockPriceDataSource,
    private val defaultDispatcher: CoroutineDispatcher
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            StockPriceDataSource::class.java,
            CoroutineDispatcher::class.java
        )
            .newInstance(stockPriceDataSource, defaultDispatcher)
    }
}