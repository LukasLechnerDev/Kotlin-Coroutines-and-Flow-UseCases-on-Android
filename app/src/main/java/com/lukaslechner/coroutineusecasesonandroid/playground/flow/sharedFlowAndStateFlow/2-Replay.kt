package com.lukaslechner.coroutineusecasesonandroid.playground.flow.sharedFlowAndStateFlow

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

private fun replaySharedFlow(): Flow<Int> {
    val sharedFlow = MutableSharedFlow<Int>(replay = 2)

    GlobalScope.launch {
        repeat(5) {
            sharedFlow.emit(Random.nextInt(1, 50))
            delay(500)
        }
    }

    return sharedFlow
}

fun main() = runBlocking<Unit> {
    val replaySharedFlow = replaySharedFlow()

    launch {
        replaySharedFlow.collect {
            println("First Collector collected: $it")
        }
        println("First Collector finished collection.")
    }

    launch {
        delay(1200)
        replaySharedFlow.collect {
            println("Second Collector collected: $it")
        }
        println("Second Collector finished collection.")
    }
}