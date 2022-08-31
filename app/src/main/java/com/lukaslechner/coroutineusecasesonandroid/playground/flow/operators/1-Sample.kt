package com.lukaslechner.coroutineusecasesonandroid.playground.flow.operators

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.runBlocking

private val fastEmittingFlow = flow {
    repeat(10) {
        emit(it)
        delay(110)
    }
}

fun main() = runBlocking {
    fastEmittingFlow.sample(200).collect {
        println("$it")
    }

    // Returns a flow that emits only the latest value emitted by the original flow during the given sampling period.

    // prints: 1,3,5,6,7
}