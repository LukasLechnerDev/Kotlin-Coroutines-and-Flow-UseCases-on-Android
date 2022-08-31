package com.lukaslechner.coroutineusecasesonandroid.playground.flow.retry

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main() = runBlocking {
    getRandom()
        .retryWhen { exception, retryAttempt ->
            // if the lambda expression returns true, the flow is retried
            // if the lambda expression returns false, the exception proceeds to the collector
            retryAttempt < 2

            // we could e.g. only retry for certain exceptions
        }
        .collect {
            // 1 emissions, 1 initial and 2 retries
            println("Collected: $it")
        }
}

private fun getRandom() = flow {
    repeat(3) {
        delay(100)
        emit(Random.nextInt())
        check(it < 0)
    }
}