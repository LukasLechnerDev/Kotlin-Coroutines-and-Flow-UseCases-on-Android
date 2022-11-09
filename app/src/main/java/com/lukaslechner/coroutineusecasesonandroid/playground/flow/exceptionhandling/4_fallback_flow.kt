package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {

    launch {
        val stocksFlow = stocksFlow()

        stocksFlow
            .onCompletion { cause ->
                if (cause == null) {
                    println("Flow completed successfully!")
                } else {
                    println("Flow completed exceptionally with $cause")
                }
            }
            .catch { throwable ->
                println("Handle exception in catch() operator $throwable")
                emitAll(fallbackFlow())
            }.catch { throwable ->
                println("Handle exception in 2. catch() operator $throwable")
            }
            .collect { stock ->
                println("Collected $stock")
            }
    }
}

private fun stocksFlow(): Flow<String> = flow {
    emit("Apple")
    emit("Microsoft")

    throw Exception("Network Request Failed!")
}

private fun fallbackFlow(): Flow<String> = flow {
    emit("Fallback Stock")

    throw Exception("Exception in Fallback Flow")
}