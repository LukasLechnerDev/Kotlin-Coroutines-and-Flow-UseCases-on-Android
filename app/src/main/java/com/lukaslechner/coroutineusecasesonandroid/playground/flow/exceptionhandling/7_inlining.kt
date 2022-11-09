package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

suspend fun main(): Unit = coroutineScope {

    flow {
        emit(1)
        emit(2)
        emit(3)
    }.collect {
        println("Collected $it")
    }
}

val inlinedFlow = flow<Int> {
    println("Collected 1")
    println("Collected 2")
    println("Collected 3")
}