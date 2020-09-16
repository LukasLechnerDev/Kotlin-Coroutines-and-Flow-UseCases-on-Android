package com.lukaslechner.coroutineusecasesonandroid.playground.testing

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test


class SystemUnderTest {

    suspend fun functionWithDelay(): Int {
        delay(1000)
        return 42
    }
}

fun CoroutineScope.functionThatStartsNewCoroutine() {
    launch {
        delay(1000)
        println("Coroutine completed!")
    }
}

class TestClass {

    @Test
    fun `functionWithDelay should return 42`() = runBlockingTest {

        val realTimeStart = System.currentTimeMillis()
        val virtualTimeStart = currentTime

        functionThatStartsNewCoroutine()
        advanceTimeBy(1000)

        val realTimeDuration = System.currentTimeMillis() - realTimeStart
        val virtualTimeDuration = currentTime - virtualTimeStart

        println("Test took $realTimeDuration real ms")
        println("Test took $virtualTimeDuration virtual ms")
    }

}