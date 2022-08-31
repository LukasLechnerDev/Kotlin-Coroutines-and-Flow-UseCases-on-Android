package com.lukaslechner.coroutineusecasesonandroid.playground.flow.sharedFlowAndStateFlow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlin.random.Random

// Return type is Flow so that no one can call emit on the call site.
// We could also define SharedFlow as ReturnType, then the call site could call emit() on it.
private fun sharedFlow(): Flow<Int> {
    val sharedFlow = MutableSharedFlow<Int>()

    GlobalScope.launch(Dispatchers.Default) {
        repeat(3) {
            sharedFlow.emit(Random.nextInt(0, 50))
            delay(500)
        }
    }

    return sharedFlow
}

fun main() = runBlocking<Unit> {
    val sharedFlow = sharedFlow()

    launch {
        sharedFlow.collect {
            println("First collector: collected: $it")
        }
        println("First collector stopped")
    }

    launch {
        delay(600)
        sharedFlow.collect {
            println("Second collector: collected $it")
        }
        println("Second collector stopped.")
    }

    // Each collector gets the same number, since they subscribe to the same shared flow
}