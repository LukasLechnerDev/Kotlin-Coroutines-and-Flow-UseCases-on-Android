package com.lukaslechner.coroutineusecasesonandroid.playground

fun main() {

    val startTime = System.currentTimeMillis()
    println("Running on Thread: ${Thread.currentThread().name}")

    Thread.sleep(1000)

    // Thread is blocked and cannot perform other work
    // This could be a blocking I/O call, like a synchronous Retrofit call
    // Or a heavy computation

    // Blocking a thread is only bad when you block a critical thread, like the UI thread on Android
    // because the UI will freeze

    val executionTime = System.currentTimeMillis() - startTime
    println("Application finished in ${executionTime}ms")
}
