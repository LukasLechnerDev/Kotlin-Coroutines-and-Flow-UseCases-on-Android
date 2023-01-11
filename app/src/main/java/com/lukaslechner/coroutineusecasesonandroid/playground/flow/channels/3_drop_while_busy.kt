package com.lukaslechner.coroutineusecasesonandroid.playground.flow.channels

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 UseCase: We want to trigger some processing. While the downstream is currently
 busy, we want to drop the current trigger emission.

 With a SharedFlow, this is not possible, since we need to define a buffer size of
 > 0 for buffer strategies like "DROP_LATEST". Channels however have a buffer sice of 0
 by default.

 Another option is to use the custom operator "dropIfBusy" (see below)

 See also: https://stackoverflow.com/questions/64844821/how-to-drop-latest-with-coroutine-flowt/74560222#74560222
**/

fun <T> Flow<T>.dropIfBusy(): Flow<T> = flow {
    coroutineScope {
        val channel = produce {
            collect { trySend(it) }
        }
        channel.consumeEach { emit(it) }
    }
}

suspend fun main(): Unit = coroutineScope {

    val channel = Channel<Int>()

    launch {
        channel
            .consumeAsFlow()
            .collect {
                println("Process $it")
                delay(1000)
                println("$it processed")
            }
    }

    launch {

        delay(100)

        // 1 should be processed
        channel.trySend(1)
        println("sharedFlow emits 1")

        // 2 should not be processed since downstream is busy
        channel.trySend(2)
        println("sharedFlow emits 2")

        // 3 should be processed again
        delay(2000)
        channel.trySend(3)
        println("sharedFlow emits 3")
    }
}