package com.lukaslechner.coroutineusecasesonandroid.playground.flow.basics

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

private fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: Start")
    delay(500) // wait 500 ms
    emit("$i: End")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    //flatMapConcat is used for maintaining Order
    val startTime = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // emit a number every 100 ms
        .flatMapConcat { requestFlow(it) }
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}