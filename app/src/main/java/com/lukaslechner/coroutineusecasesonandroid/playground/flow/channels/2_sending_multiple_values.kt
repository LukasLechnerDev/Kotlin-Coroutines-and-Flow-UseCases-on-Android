package com.lukaslechner.coroutineusecasesonandroid.playground.flow.channels

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {

    val channel = produce<Int> {
        println("Sending 10")
        send(10)

        println("Sending 20")
        send(20)
    }

    launch {
        channel.consumeEach { receivedValue ->
            println("Consumer1: $receivedValue")
        }
    }

    launch {
        channel.consumeEach { receivedValue ->
            println("Consumer2: $receivedValue")
        }
    }
}