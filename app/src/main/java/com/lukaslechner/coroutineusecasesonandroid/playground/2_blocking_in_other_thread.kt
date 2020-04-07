package com.lukaslechner.coroutineusecasesonandroid.playground

import kotlin.concurrent.thread

fun main() {

    val startTime = System.currentTimeMillis()
    println("Running on Thread: ${Thread.currentThread().name}")

    val thread = thread {
        println("performing blocking operation on Thread: ${Thread.currentThread().name}")
        Thread.sleep(1000)
    }

    thread.join()

    val executionTime = System.currentTimeMillis() - startTime
    println("Application finished in ${executionTime}ms")
}
