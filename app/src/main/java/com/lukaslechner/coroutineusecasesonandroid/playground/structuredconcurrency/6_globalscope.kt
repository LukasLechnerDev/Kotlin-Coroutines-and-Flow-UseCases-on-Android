package com.lukaslechner.coroutineusecasesonandroid.playground.structuredconcurrency

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main() {

    println("Job of GlobalScope: ${GlobalScope.coroutineContext[Job]}")

    GlobalScope.launch {

    }

}