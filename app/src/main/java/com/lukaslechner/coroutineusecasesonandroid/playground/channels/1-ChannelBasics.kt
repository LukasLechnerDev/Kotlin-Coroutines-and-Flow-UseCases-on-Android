package com.lukaslechner.coroutineusecasesonandroid.playground.channels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random


fun main() {
    GlobalScope.launch {
        produceRandom().consumeEach { println(it) }
        println("All values consumed")
    }

    println("main() finished")
    Thread.sleep(1000)
}

// whereas flow() is a top level function => produce is defined on CoroutineScope
private fun CoroutineScope.produceRandom() = produce {
    repeat(5) {
        delay(100)
        send(Random.nextInt())
    }
}