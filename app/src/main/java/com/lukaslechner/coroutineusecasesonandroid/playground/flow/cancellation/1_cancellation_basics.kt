package com.lukaslechner.coroutineusecasesonandroid.playground.flow.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main() {
    val scope = CoroutineScope(EmptyCoroutineContext)

    scope.launch {
        intFlow()
            .onCompletion { throwable ->
                if (throwable is CancellationException) {
                    println("Flow got cancelled.")
                }
            }
            .collect {
                println("Collected $it")
            }
    }.join()
}

private fun intFlow() = flow {
    emit(1)
    emit(2)
    emit(3)
}