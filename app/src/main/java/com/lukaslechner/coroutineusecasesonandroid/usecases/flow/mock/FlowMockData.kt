package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock

import kotlin.random.Random

data class GoogleStock(val currentPriceUsd: Float)

fun fakeCurrentGoogleStockPrice(): GoogleStock {
    val initialPrice = 2_000f
    val increase = Random.nextInt(100)
    val currentPrice = initialPrice + increase
    return GoogleStock(currentPrice)
}