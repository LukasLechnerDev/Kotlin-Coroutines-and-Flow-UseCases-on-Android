package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.*

fun main() {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught exception: $throwable")
    }

    val scope = CoroutineScope(Job())

    scope.launch(exceptionHandler) {
        launch {
            println("Starting coroutine 1")
            delay(100)
            throw RuntimeException()
        }
        launch {
            println("Starting coroutine 2")
            delay(3000)
            println("Coroutine 2 completed")
        }
    }

    Thread.sleep(5000)

}