package com.lukaslechner.coroutineusecasesonandroid.playground.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

private fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

private fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        Thread.sleep(100) // pretend we are computing it in CPU-consuming way
        log("Emitting $i")
        emit(i) // emit next value
    }
}.flowOn(Dispatchers.Default) // RIGHT way to change context for CPU-consuming code in flow builder

// flowOn creates another coroutine for an upstream flow when it has to change the CoroutineDispatcher in its context

// Notice how flow{} works in the background thread, while collection happens in the main thread.

// Another thing to observe here is that the flowOn operator has changed the default sequential nature of the flow.
// Now, collection happens in one coroutine, and emission happens in another coroutine that is running in another thread concurrently with
// the collecting coroutine

fun main() = runBlocking<Unit> {
    simple().collect { value ->
        log("Collected $value")
    }
}