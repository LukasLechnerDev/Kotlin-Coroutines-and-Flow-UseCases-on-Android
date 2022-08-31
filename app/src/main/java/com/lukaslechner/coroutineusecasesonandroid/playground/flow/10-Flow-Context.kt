package com.lukaslechner.coroutineusecasesonandroid.playground.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

// Context preservation
// Collection of a flow always happens in the context of the calling coroutine.

private fun simple(): Flow<Int> = flow {
    log("Started simple flow")
    for (i in 1..3) {
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    simple().collect { value -> log("Collected $value") }
}