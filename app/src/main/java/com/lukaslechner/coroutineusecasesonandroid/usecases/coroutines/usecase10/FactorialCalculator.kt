package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import kotlinx.coroutines.*
import java.math.BigInteger

class FactorialCalculator(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun calculateFactorial(factorialOf: Int, numberOfThreads: Int): BigInteger =
        coroutineScope {
            val subRanges = createSubRangeList(factorialOf, numberOfThreads)
            withContext(Dispatchers.Default) {
                val result = subRanges.map { subRange ->
                    async {
                        val result = calculateFactorialOfSubRange(
                            subRange
                        )
                        result
                    }
                }.awaitAll().fold(BigInteger.ONE, BigInteger::multiply)
                return@withContext result
            }
        }


    suspend fun calculateFactorialOfSubRange(subRange: SubRange): BigInteger {
        return withContext(defaultDispatcher) {
            var factorial = BigInteger.ONE
            for (i in subRange.start..subRange.end) {
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            factorial
        }
    }

    suspend fun createSubRangeList(factorialOf: Int, numberOfSubRanges: Int): List<SubRange> =
        withContext(defaultDispatcher) {
            val floor = factorialOf.div(numberOfSubRanges)
            val rangesList = mutableListOf<SubRange>()

            var curStartIndex = 1
            repeat(numberOfSubRanges - 1) {
                rangesList.add(
                    SubRange(
                        curStartIndex,
                        curStartIndex + (floor - 1)
                    )
                )
                curStartIndex += floor
            }
            rangesList.add(SubRange(curStartIndex, factorialOf))
            rangesList
        }
}

data class SubRange(val start: Int, val end: Int)