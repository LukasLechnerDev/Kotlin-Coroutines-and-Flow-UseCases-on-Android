package com.lukaslechner.coroutineusecasesonandroid.playground.flow.cancellation

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

// You could add .onEach{ currentCoroutineContext().ensureActive() }
// but there is a ready to use cancellable operator provided to do that:
fun main() = runBlocking<Unit> {
    (1..5).asFlow()
        .cancellable() // ready to use cancellable operator
        .collect { value ->
            if (value == 3) cancel()
            println(value)
        }
}