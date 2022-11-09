package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Stock

val appleStock = Stock(
    rank = 1,
    name = "Apple",
    symbol = "AAPL",
    marketCap = 1.0f,
    country = "United Stated",
    currentPrice = 1.0f
)

val googleStock = Stock(
    rank = 4,
    name = "Alphabet",
    symbol = "GOOG",
    marketCap = 2.0f,
    country = "United Stated",
    currentPrice = 2.0f
)
