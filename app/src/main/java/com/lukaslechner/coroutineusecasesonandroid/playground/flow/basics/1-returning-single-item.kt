package com.lukaslechner.coroutineusecasesonandroid.playground.flow.basics

import java.math.BigInteger

fun main() {
    val result = calculateFactorialOf(5)
    println("Result: $result")
}

// factorial of n (n!) = 1 * 2 * 3 * 4 * ... * n
private fun calculateFactorialOf(number: Int): BigInteger {
    var factorial = BigInteger.ONE
    for (i in 1..number) {
        Thread.sleep(10)
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
    }
    return factorial
}