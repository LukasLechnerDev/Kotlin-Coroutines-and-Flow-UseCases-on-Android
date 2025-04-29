package com.lukaslechner.coroutineusecasesonandroid.playground.flow.concurrency

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

fun main() = runBlocking {

//    println("Replay and extraBufferCapacity must not be zero in BufferOverflow.DROP_OLDEST and BufferOverflow.DROP_LATEST ")
//    replayAndCapacityMustNotBeZero()

    println("Collect After Delay")
    collectAfterDelay()

}

suspend fun replayAndCapacityMustNotBeZero() = coroutineScope {
    // replay and extraBufferCapacity throw exception ==> replay or extraBufferCapacity must be positive with non-default onBufferOverflow strategy DROP_LATEST
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 0,
//        extraBufferCapacity = 0,
//        onBufferOverflow = BufferOverflow.DROP_LATEST
//    )

    // Collected value is A,B,C,D,E
    val sharedFlow = MutableSharedFlow<String>(
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    //Emit all A to E and Collected one by one A,B,C,D,E
//    val sharedFlow = MutableSharedFlow<String>(
//        extraBufferCapacity = 5,
//        onBufferOverflow = BufferOverflow.SUSPEND
//    )

    // Collected value is A,B,C
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 1,
//        extraBufferCapacity = 2,
//        onBufferOverflow = BufferOverflow.DROP_LATEST
//    )

    // Collected value is A,B,C,D,E
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 2,
//        extraBufferCapacity = 3,
//        onBufferOverflow = BufferOverflow.DROP_LATEST
//    )

    // Collected value is C,D,E
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 1,
//        extraBufferCapacity = 2,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )

    // Collected value is A,B,C,D,E
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 2,
//        extraBufferCapacity = 3,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )

    val collector1 = launch {
        sharedFlow.collect {
            println("Processing $it")
            delay(500)
            println("Collected One (SUSPEND): $it")
        }
    }

    val emitter = launch {
        emitValues(sharedFlow)
    }

    joinAll(emitter, collector1)
}

suspend fun collectAfterDelay() = coroutineScope {
    // replay and extraBufferCapacity throw exception ==> replay or extraBufferCapacity must be positive with non-default onBufferOverflow strategy DROP_LATEST
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 0,
//        extraBufferCapacity = 0,
//        onBufferOverflow = BufferOverflow.DROP_LATEST
//    )

    // Collected value None
//    val sharedFlow = MutableSharedFlow<String>(
//        onBufferOverflow = BufferOverflow.SUSPEND
//    )

    // Collected value None
//    val sharedFlow = MutableSharedFlow<String>(
//        extraBufferCapacity = 3,
//        onBufferOverflow = BufferOverflow.SUSPEND
//    )

    // Collected value C,D,E
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 3,
//        onBufferOverflow = BufferOverflow.SUSPEND
//    )


    // Collected value is E
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 1,
//        extraBufferCapacity = 2,
//        onBufferOverflow = BufferOverflow.DROP_LATEST
//    )

    // Collected value is D,E
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 2,
//        extraBufferCapacity = 3,
//        onBufferOverflow = BufferOverflow.DROP_LATEST
//    )

    // Collected value is E
//    val sharedFlow = MutableSharedFlow<String>(
//        replay = 1,
//        extraBufferCapacity = 2,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST
//    )

    // Collected value is D,E
    val sharedFlow = MutableSharedFlow<String>(
        replay = 2,
        extraBufferCapacity = 3,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val emitter = launch {
        emitValues(sharedFlow)
    }

    delay(500)

    val collector1 = launch {
        sharedFlow.collect {
            println("Processing $it")
            delay(500)
            println("Collected One (SUSPEND): $it")
        }
    }

    joinAll(emitter, collector1)
}

suspend fun emitValues(sharedFlow: MutableSharedFlow<String>) {
    println("Emitting A")
    sharedFlow.emit("A")
    println("Emitting B")
    sharedFlow.emit("B")
    println("Emitting C")
    sharedFlow.emit("C")
    println("Emitting D")
    sharedFlow.emit("D")
    println("Emitting E")
    sharedFlow.emit("E") // Behavior depends on BufferOverflow strategy
    println("Done emitting")
}

