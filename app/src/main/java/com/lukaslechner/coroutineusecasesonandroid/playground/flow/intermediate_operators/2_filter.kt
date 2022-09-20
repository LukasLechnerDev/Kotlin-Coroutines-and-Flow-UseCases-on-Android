package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOf

suspend fun main() {

    flowOf(1, 2, 3, 4, 5)
        .filterIsInstance<Int>()
        .collect { collectedValue ->
            println(collectedValue)
        }

}