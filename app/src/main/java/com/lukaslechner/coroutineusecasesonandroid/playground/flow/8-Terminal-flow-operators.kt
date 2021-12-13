package com.lukaslechner.coroutineusecasesonandroid.playground.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.runBlocking

/**
 * All terminal operators are suspend functions
 */
fun main() = runBlocking {
    val sum = (1..5).asFlow()
        .map { it * it } // squares of numbers from 1 to 5
        .reduce { a, b -> a + b } // sum them (terminal operator); .collect doesn't need to be called
    println(sum)
}

// Other Terminal operators
// * toList(), toSet() => collects all of the objects emitted by the Flow and returns them in a List or a Set; works for bounded flows, but not for flows that emit object indefinitely
// * reduce(), flow
// * single() => returns first object emitted by the Flow, throws exception if more items are emmited
// * first() => returns one object of the flow and then stops consuming; safe to use with a Flow that might return more than one value