package com.lukaslechner.coroutineusecasesonandroid.playground.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

    val numbers = (1..3).asFlow()
        .onEach { delay(300) }

    val strings = flowOf("one", "two", "three")
        .onEach { delay(400) }

    val startTime = System.currentTimeMillis() // remember the start time
    numbers.combine(strings) { a, b -> "$a -> $b" } // compose a single string with "zip"
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}