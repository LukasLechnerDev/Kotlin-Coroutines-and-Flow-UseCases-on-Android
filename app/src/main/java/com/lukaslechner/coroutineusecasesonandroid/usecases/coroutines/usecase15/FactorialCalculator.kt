package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase15

import com.lukaslechner.coroutineusecasesonandroid.utils.addCoroutineDebugInfo
import kotlinx.coroutines.*
import timber.log.Timber
import java.math.BigInteger

class FactorialCalculator {

    suspend fun calculateFactorial(
        factorialOf: Int,
        numberOfThreads: Int,
        dispatcher: CoroutineDispatcher,
        yieldDuringCalculation: Boolean
    ): BigInteger {

        val subRanges = createSubRangeList(factorialOf, numberOfThreads, dispatcher)
        return withContext(dispatcher) {
            subRanges.map { subRange ->
                async {
                    calculateFactorialOfSubRange(subRange, yieldDuringCalculation)
                }
            }.awaitAll()
                .fold(BigInteger.ONE, { acc, element ->
                    if (yieldDuringCalculation) {
                        yield()
                    }
                    acc.multiply(element)
                })
        }
    }

    private suspend fun calculateFactorialOfSubRange(
        subRange: SubRange,
        yieldDuringCalculation: Boolean
    ): BigInteger {
        Timber.d(addCoroutineDebugInfo("Calculate factorial of $subRange"))
        var factorial = BigInteger.ONE
        for (i in subRange.start..subRange.end) {
            if (yieldDuringCalculation) {
                yield()
            }
            factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        }
        return factorial
    }

    private suspend fun createSubRangeList(
        factorialOf: Int,
        numberOfSubRanges: Int,
        dispatcher: CoroutineDispatcher
    ): List<SubRange> =
        withContext(dispatcher) {
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