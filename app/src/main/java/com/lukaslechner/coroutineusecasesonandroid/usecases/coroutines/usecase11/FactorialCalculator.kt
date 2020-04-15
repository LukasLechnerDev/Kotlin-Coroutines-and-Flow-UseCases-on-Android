package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import com.lukaslechner.coroutineusecasesonandroid.utils.addCoroutineDebugInfo
import kotlinx.coroutines.*
import timber.log.Timber
import java.math.BigInteger

class FactorialCalculator(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun calculateFactorial(
        factorialOf: Int,
        numberOfThreads: Int
    ): BigInteger {

        val subRanges = createSubRangeList(factorialOf, numberOfThreads)
        return withContext(defaultDispatcher) {
            subRanges.map { subRange ->
                async {
                    calculateFactorialOfSubRange(subRange)
                }
            }.awaitAll()
                .fold(BigInteger.ONE, { acc, element ->
                    ensureActive()
                    acc.multiply(element)
                })
        }
    }

    suspend fun calculateFactorialOfSubRange(
        subRange: SubRange
    ): BigInteger {
        return withContext(defaultDispatcher) {
            Timber.d(addCoroutineDebugInfo("Calculate factorial of $subRange"))
            var factorial = BigInteger.ONE
            for (i in subRange.start..subRange.end) {
                ensureActive()
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            factorial
        }
    }

    suspend fun createSubRangeList(
        factorialOf: Int,
        numberOfSubRanges: Int
    ): List<SubRange> =
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