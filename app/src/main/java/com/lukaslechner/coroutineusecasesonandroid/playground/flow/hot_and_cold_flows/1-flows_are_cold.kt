package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flows

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

fun coldFlow() = flow {
    println("Emitting 1")
    emit(1)

    println("Emitting 2")
    emit(2)

    println("Emitting 3")
    emit(3)
}.onEach {
    delay(1000)
}

suspend fun main(): Unit = coroutineScope {

    coldFlow()
        .collect {
            println("Collector 1 collects: $it")
        }
}
