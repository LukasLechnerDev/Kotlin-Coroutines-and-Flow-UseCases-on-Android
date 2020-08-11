package com.lukaslechner.coroutineusecasesonandroid.playground.cancellation

import kotlinx.coroutines.*

fun main() = runBlocking<Unit> {

    val job = launch(Dispatchers.Default) {
        repeat(10) { index ->
            if (isActive) {
                println("operation number $index")
                Thread.sleep(100)
            } else {
                // perform some cleanup on cancellation
                withContext(NonCancellable) {
                    delay(100)
                    println("Clean up done!")
                }
                throw CancellationException()
            }
        }
    }

    delay(250)
    println("Cancelling Coroutine")
    job.cancel()

    val globalCoroutineJob = GlobalScope.launch {
        repeat(10) {
            println("$it")
            delay(100)
        }
    }
    delay(250)
    globalCoroutineJob.cancel()
    delay(1000)
}