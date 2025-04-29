package com.lukaslechner.coroutineusecasesonandroid.playground.flow.basics

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // wait 500 ms
    emit("$i: Second")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    // flatMapMerge is not maintaining Order
    val startTime = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
        .flatMapMerge { requestFlow(it) }
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}