package com.lukaslechner.coroutineusecasesonandroid.playground.flow.completion

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

private fun simple(): Flow<Int> = flow {
    emit(1)
    throw RuntimeException()
}

fun main() = runBlocking<Unit> {
    simple()
        //.onCompletion{ } is called before .catch{ }
        //.onCompletion{ } doesn't handle the exception, the exception still flows downstream
        .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") }
        .catch { cause -> println("Caught exception") }
        .collect { value -> println(value) }
}