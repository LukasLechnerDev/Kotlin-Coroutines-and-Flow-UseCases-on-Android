package com.lukaslechner.coroutineusecasesonandroid.playground

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val startTime = System.currentTimeMillis()
    println("Running on Thread: ${Thread.currentThread().name}")

    val job = launch(Dispatchers.Default) {
        println("performing suspending operation on Thread: ${Thread.currentThread().name}")
        delay(1000)
    }

    job.join()

    val executionTime = System.currentTimeMillis() - startTime
    println("Application finished in ${executionTime}ms")
}
