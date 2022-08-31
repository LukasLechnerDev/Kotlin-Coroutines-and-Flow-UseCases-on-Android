package com.lukaslechner.coroutineusecasesonandroid.playground.channels

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlin.random.Random


fun main() = runBlocking {

    // Buffer of a default size of 64 elements
    val channel = Channel<Int>(Channel.BUFFERED)

    val job1 = GlobalScope.launch(Dispatchers.Default) {
        repeat(3) {
            delay(500)
            channel.send(Random.nextInt(1, 100))
        }

        channel.close()
    }

    val job2 = GlobalScope.launch() {
        channel.consumeEach {
            println(it)
            println("Consumed")
        }
    }

    joinAll(job1, job2)
}