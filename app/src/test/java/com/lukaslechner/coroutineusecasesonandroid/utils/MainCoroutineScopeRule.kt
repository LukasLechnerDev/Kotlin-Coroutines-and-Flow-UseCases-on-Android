package com.lukaslechner.coroutineusecasesonandroid.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit Rule that replaces the Main Dispatcher with a test dispatcher in your tests
 *
 * @see: https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/
 * @see: https://github.com/googlecodelabs/kotlin-coroutines/blob/master/coroutines-codelab/finished_code/src/test/java/com/example/android/kotlincoroutines/main/utils/MainCoroutineScopeRule.kt
 */
@ExperimentalCoroutinesApi
class MainCoroutineScopeRule(
    val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher(),
    TestCoroutineScope by TestCoroutineScope(testDispatcher) {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}