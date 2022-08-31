package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock

import kotlin.random.Random

data class BitcoinPrice(val usd: Float)

data class EtheriumPrice(val usd: Float)

fun fakeCurrentBitcoinPrice(): BitcoinPrice {
    val initialPrice = 50_000f
    val increase = Random.nextInt(1000)
    val currentPrice = initialPrice + increase
    return BitcoinPrice(currentPrice)
}