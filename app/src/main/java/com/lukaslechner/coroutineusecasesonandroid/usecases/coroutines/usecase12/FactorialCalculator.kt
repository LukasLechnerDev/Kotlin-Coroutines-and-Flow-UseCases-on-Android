package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12

import com.lukaslechner.coroutineusecasesonandroid.utils.addCoroutineDebugInfo
import kotlinx.coroutines.*
import timber.log.Timber
import java.math.BigInteger

class FactorialCalculator(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun calculateFactorial(
        factorialOf: Int,
        numberOfCoroutines: Int
    ): BigInteger {
        return withContext(defaultDispatcher) {
            val subRanges = createSubRangeList(factorialOf, numberOfCoroutines)
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

    fun createSubRangeList(
        factorialOf: Int,
        numberOfSubRanges: Int
    ): List<SubRange> {
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
        return rangesList
    }
}

data class SubRange(val start: Int, val end: Int)