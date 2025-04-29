package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

suspend fun main(): Unit = coroutineScope {

    val stocksFlow = stocksFlow()

    stocksFlow
        .onCompletion { cause ->
            if (cause == null) {
                println("Flow completed successfully!")
            } else {
                println("Flow completed exceptionally with $cause")
            }
        }
        .onEach { stock ->
            throw Exception("Exception in collect{}")
            println("Collected $stock")
        }.catch { throwable ->
            println("Handle exception in catch() operator $throwable")
        }
        .launchIn(this)
    println("Done")
}

private fun stocksFlow(): Flow<String> = flow {
    emit("Apple")
    emit("Microsoft")

    throw Exception("Network Request Failed!")
}