package com.lukaslechner.coroutineusecasesonandroid.playground

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val executionTime = measureTimeMillis {
        val jobs = List(100_000) {
            launch {
                delay(10000L)
                print(".")
            }
        }
        jobs.forEach { it.join() }
    }
    println("\nExecution took ${executionTime}ms")
}