package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AndroidVersionRepositoryTest {

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    @Test
    fun `getLocalAndroidVersions() should return android versions from database`() =
        mainCoroutineScopeRule.runBlockingTest {
            val fakeDatabase = FakeDatabase()

            val repository = AndroidVersionRepository(fakeDatabase, mainCoroutineScopeRule)
            assertEquals(mockAndroidVersions, repository.getLocalAndroidVersions())
        }

    @Test
    fun `loadRecentAndroidVersions() should return android versions from network`() =
        mainCoroutineScopeRule.runBlockingTest {
            val fakeDatabase = FakeDatabase()
            val fakeApi = FakeApi()
            val repository = AndroidVersionRepository(
                fakeDatabase,
                mainCoroutineScopeRule,
                api = fakeApi
            )
            assertEquals(mockAndroidVersions, repository.loadAndStoreRemoteAndroidVersions())
        }

    @Test
    fun `loadRecentAndroidVersions() should continue to load and store android versions when calling scope gets cancelled`() =
        mainCoroutineScopeRule.runBlockingTest {
            val fakeDatabase = FakeDatabase()
            val fakeApi = FakeApi()
            val repository = AndroidVersionRepository(
                fakeDatabase,
                mainCoroutineScopeRule,
                api = fakeApi
            )

            // this coroutine will be executed immediately (eagerly)
            // how ever, it will stop its execution at the delay(1) in the fakeApi
            val viewModelScope = TestCoroutineScope(SupervisorJob())
            viewModelScope.launch {
                println("running coroutine!")
                repository.loadAndStoreRemoteAndroidVersions()
                fail("Scope should be cancelled before versions are loaded!")
            }

            viewModelScope.cancel()

            // continue coroutine execution after delay(1) in the fakeApi
            advanceUntilIdle()

            assertEquals(true, fakeDatabase.insertedIntoDb)
        }
}