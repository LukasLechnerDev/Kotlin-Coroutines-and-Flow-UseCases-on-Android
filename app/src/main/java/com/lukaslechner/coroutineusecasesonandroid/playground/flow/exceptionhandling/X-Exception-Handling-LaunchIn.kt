package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

    val someScope = CoroutineScope(Dispatchers.Default)

    var job: Job? = null

    try {
        job = someFlow.onEach {
            println("onEach: $it")
        }.launchIn(someScope)
    } catch (exception: Exception) {
        println("Caught: $exception")
    }

    // launchIn creates a new coroutine, so we can't use try-catch to handle exceptions

    job?.join()
}

private val someFlow = flow<Int> {
    emit(1)
    emit(2)
    throw IllegalStateException()
}