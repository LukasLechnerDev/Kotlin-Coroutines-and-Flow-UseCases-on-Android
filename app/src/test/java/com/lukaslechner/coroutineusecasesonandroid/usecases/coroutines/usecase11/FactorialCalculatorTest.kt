package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.math.BigInteger

@ExperimentalCoroutinesApi
class FactorialCalculatorTest {

    lateinit var factorialCalculator: FactorialCalculator

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        factorialCalculator = FactorialCalculator(testDispatcher)
    }

    @Test
    fun `createSubRangeList() should create correct range lists`() = runBlockingTest {
        assertEquals(
            listOf(
                SubRange(1, 3),
                SubRange(4, 6),
                SubRange(7, 9)
            ),
            factorialCalculator.createSubRangeList(9, 3)
        )

        assertEquals(
            listOf(
                SubRange(1, 3),
                SubRange(4, 6),
                SubRange(7, 10)
            ),
            factorialCalculator.createSubRangeList(10, 3)
        )

        assertEquals(
            listOf(
                SubRange(1, 3),
                SubRange(4, 6),
                SubRange(7, 11)
            ),
            factorialCalculator.createSubRangeList(11, 3)
        )
    }

    @Test
    fun calculateFactorialOfSubRange() = runBlockingTest {
        assertEquals(
            BigInteger.valueOf(6),
            factorialCalculator.calculateFactorialOfSubRange(SubRange(1, 3))
        )
        assertEquals(
            BigInteger.valueOf(120),
            factorialCalculator.calculateFactorialOfSubRange(SubRange(4, 6))
        )
        assertEquals(
            BigInteger.valueOf(55440),
            factorialCalculator.calculateFactorialOfSubRange(SubRange(7, 11))
        )
    }

    @Test
    fun calculateFactorial() = runBlockingTest {
        assertEquals(
            BigInteger.valueOf(3628800),
            factorialCalculator.calculateFactorial(10, 3)
        )
    }
}