package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock

import android.content.Context
import com.opencsv.CSVReader
import java.io.InputStreamReader
import kotlin.random.Random

data class Stock(
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCap: Float,
    val country: String,
    val currentPrice: Float,
    val currency: Currency = Currency.DOLLAR,
    val priceTrend: PriceTrend = PriceTrend.UNKNOWN
)

enum class Currency {
    DOLLAR, EURO
}

enum class PriceTrend {
    UP, DOWN, NEUTRAL, UNKNOWN
}

var initialStockData: List<Stock>? = null

fun readAndParseStockData(context: Context) {
    if (initialStockData == null) {
        val stream = context.assets.open("stockdata.csv")
        val csvReader = CSVReader(InputStreamReader(stream))
        val stockData = csvReader
            .readAll()
            .drop(1)
            .mapNotNull { line ->
                val rank = line[0].toInt()
                val name = line[1]
                val symbol = line[2]
                val marketCap = line[3].toFloat()
                val priceUsd = line[4].toFloat()
                val country = line[5]
                Stock(rank, name, symbol, marketCap, country, priceUsd)
            }.also {
                csvReader.close()
            }
        initialStockData = stockData
    }
}

fun fakeCurrentStockPrices(context: Context): List<Stock> {
    readAndParseStockData(context)

    return initialStockData!!.map { stock ->
        val currentPrice = stock.currentPrice
        if (stock.currentPrice == 0f) {
            return@map stock
        }
        val randomRangeInPercent = 0.03
        val randomLowerBound = (currentPrice * (1 - randomRangeInPercent))
        val randomUpperBound = (currentPrice * (1 + randomRangeInPercent))
        val randomPrice = Random.nextDouble(randomLowerBound, randomUpperBound).toFloat()
        stock.copy(currentPrice = randomPrice)
    }
}