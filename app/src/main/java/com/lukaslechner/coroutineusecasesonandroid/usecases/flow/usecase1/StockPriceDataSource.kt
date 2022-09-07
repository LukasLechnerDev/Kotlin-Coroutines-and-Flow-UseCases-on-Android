package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.FlowMockApi
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Stock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface StockPriceDataSource {
    val latestStockList: Flow<List<Stock>>
}

class NetworkStockPriceDataSource(mockApi: FlowMockApi) : StockPriceDataSource {

    override val latestStockList: Flow<List<Stock>> = flow {
        while (true) {
            val currentStockList = mockApi.getCurrentStockPrices()
            emit(currentStockList)
            delay(5_000)
        }
    }
}