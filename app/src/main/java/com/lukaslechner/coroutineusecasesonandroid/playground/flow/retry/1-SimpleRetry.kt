package com.lukaslechner.coroutineusecasesonandroid.playground.flow.retry

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main() = runBlocking {
    getRandom()
        .retry(3)
        .collect {
            // 4 emissions, 1 initial and 3 retries
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