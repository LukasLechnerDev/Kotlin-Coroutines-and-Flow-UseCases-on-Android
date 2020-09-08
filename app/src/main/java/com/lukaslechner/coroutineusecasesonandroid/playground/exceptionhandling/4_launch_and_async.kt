package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.*

fun main() {

    val exceptionHandler = CoroutineExceptionHandler { context, exception ->
        println("Caught $exception in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)

    scope.async {
        val deferred = async {
            delay(200)
            throw RuntimeException()
        }
    }

    Thread.sleep(1000)

}