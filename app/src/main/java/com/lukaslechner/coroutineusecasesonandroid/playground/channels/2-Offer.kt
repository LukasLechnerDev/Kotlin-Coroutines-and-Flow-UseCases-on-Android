package com.lukaslechner.coroutineusecasesonandroid.playground.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main() = runBlocking {

    val getRandomIntsChannel = generateRandom()

    delay(500)

    getRandomIntsChannel.consumeEach {
        // with offer, we will only consume 5-6 offered items
        println("Random number $it consumed")
    }

    println("Done!")
}

// Return Type is ReceiveChannel
private fun CoroutineScope.generateRandom() = produce {
    repeat(10) {
        delay(100)

        // emit in flows or send in channels will suspend until someone can receive the item
        // send(Random.nextInt())
        offer(Random.nextInt())
    }
}