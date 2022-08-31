package com.lukaslechner.coroutineusecasesonandroid.playground.flow.sharedFlowAndStateFlow

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// The difference between SharedFlow and StateFlow comes with backpressure
// Shared Flow has some options for handling backpressure
// StateFlow just replaces old values with new ones - StateFlow has single-element buffer

// StateFlow always has a value - you need to set an initial value when creating a StateFlow

// Consumers of StateFlow will get the current emission immediately.
// .value = now current value

// Good for state management

// Exposing StateFlow allows the consumer to get the last-emitted state
private fun stateFlow(): Flow<String> {
    val letters = MutableStateFlow("A")
    GlobalScope.launch {

        listOf("B", "C", "D", "E", "F").forEach { letter ->
            letters.value = letter
            delay(500)
        }

    }
    return letters
}

fun main() = runBlocking {

    stateFlow().collect {
        delay(1100) // Backpressure
        println("Collected: $it")
    }
}