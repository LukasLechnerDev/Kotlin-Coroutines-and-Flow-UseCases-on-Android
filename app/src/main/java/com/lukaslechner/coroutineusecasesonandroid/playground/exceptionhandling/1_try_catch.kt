package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main() {

    val scope = CoroutineScope(Job())
    try {
        scope.launch {
            functionThatThrowsIt()
        }
    } catch (e: Exception) {
        println("Caught: $e")
    }

    Thread.sleep(100)
}

fun functionThatThrowsIt() {
    throw RuntimeException()
}