package com.lukaslechner.coroutineusecasesonandroid.playground.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

    val numbers = (1..3).asFlow()
    val strings = flowOf("one", "two", "three")
    numbers.zip(strings) { a, b -> "$a -> $b" } // compose a single string
        .collect { println(it) } // collect and print
}