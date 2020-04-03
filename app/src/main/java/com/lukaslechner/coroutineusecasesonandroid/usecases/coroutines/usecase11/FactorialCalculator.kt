package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import kotlinx.coroutines.*
import java.math.BigInteger

class FactorialCalculator(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun calculateFactorial(factorialOf: Int, numberOfThreads: Int): BigInteger {
        val subRanges = createSubRangeList(factorialOf, numberOfThreads)
        return withContext(defaultDispatcher) {
            subRanges.map { subRange ->
                async {
                    val result = calculateFactorialOfSubRange(subRange)
                    result
                }
            }.awaitAll()
                .fold(BigInteger.ONE, { acc, element ->
                    yield()
                    acc.multiply(element)
                })
        }
    }

    suspend fun calculateFactorialOfSubRange(subRange: SubRange): BigInteger {
        return withContext(defaultDispatcher) {
            var factorial = BigInteger.ONE
            for (i in subRange.start..subRange.end) {
                yield()
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            factorial
        }
    }

    suspend fun createSubRangeList(factorialOf: Int, numberOfSubRanges: Int): List<SubRange> =
        withContext(defaultDispatcher) {
            val quotient = factorialOf.div(numberOfSubRanges)
            val rangesList = mutableListOf<SubRange>()

            var curStartIndex = 1
            repeat(numberOfSubRanges - 1) {
                rangesList.add(
                    SubRange(
                        curStartIndex,
                        curStartIndex + (quotient - 1)
                    )
                )
                curStartIndex += quotient
            }
            rangesList.add(SubRange(curStartIndex, factorialOf))
            rangesList
        }
}

data class SubRange(val start: Int, val end: Int)