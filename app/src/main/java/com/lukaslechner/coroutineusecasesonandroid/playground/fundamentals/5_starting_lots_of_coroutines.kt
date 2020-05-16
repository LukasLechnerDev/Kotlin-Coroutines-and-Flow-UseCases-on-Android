package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    repeat(1_000_000) {
        launch {
            delay(5000)
            print(".")
        }
    }
}