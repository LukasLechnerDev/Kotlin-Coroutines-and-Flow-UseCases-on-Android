package com.lukaslechner.coroutineusecasesonandroid.playground

import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun main() {
    val executionTime = measureTimeMillis {
        val jobs = List(100_000) {
            thread {
                Thread.sleep(500L)
                print(".")
            }
        }
        jobs.forEach { it.join() }
    }

    println("\nExecution took: ${executionTime}ms")
}