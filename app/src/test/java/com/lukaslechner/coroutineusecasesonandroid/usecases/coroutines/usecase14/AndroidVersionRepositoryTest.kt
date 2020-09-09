package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.EndpointShouldNotBeCalledException
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AndroidVersionRepositoryTest {

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private var insertedIntoDb = false

    @Before
    fun setUp() {
        insertedIntoDb = false
    }

    @Test
    fun `getLocalAndroidVersions() should return android versions from database`() =
        mainCoroutineScopeRule.runBlockingTest {
            val fakeDatabase = createFakeDatabase()

            val repository = AndroidVersionRepository(fakeDatabase, mainCoroutineScopeRule)
            assertEquals(mockAndroidVersions, repository.getLocalAndroidVersions())
        }

    @Test
    fun `loadRecentAndroidVersions() should return android versions from network`() =
        mainCoroutineScopeRule.runBlockingTest {
            val fakeDatabase = createFakeDatabase()
            val fakeApi = createFakeApi()
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
            val fakeDatabase = createFakeDatabase()
            val fakeApi = createFakeApi()
            val repository = AndroidVersionRepository(
                fakeDatabase,
                mainCoroutineScopeRule,
                api = fakeApi
            )

            val testScope = TestCoroutineScope(Job())

            testScope
                .launch {
                    println("running coroutine!")
                    val loadedVersions = repository.loadAndStoreRemoteAndroidVersions()
                    fail("Scope should be cancelled before versions are loaded!")
                }

            testScope.cancel()

            advanceUntilIdle()

            assertEquals(true, insertedIntoDb)
        }

    private fun createFakeApi(): MockApi {
        return object : MockApi {
            override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
                delay(100)
                return mockAndroidVersions
            }

            override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
                throw EndpointShouldNotBeCalledException()
            }
        }
    }

    private fun createFakeDatabase(): AndroidVersionDao {
        return object : AndroidVersionDao {
            override suspend fun getAndroidVersions(): List<AndroidVersionEntity> {
                return mockAndroidVersions.mapToEntityList()
            }

            override suspend fun insert(androidVersionEntity: AndroidVersionEntity) {
                insertedIntoDb = true
            }

            override suspend fun clear() {}
        }
    }
}