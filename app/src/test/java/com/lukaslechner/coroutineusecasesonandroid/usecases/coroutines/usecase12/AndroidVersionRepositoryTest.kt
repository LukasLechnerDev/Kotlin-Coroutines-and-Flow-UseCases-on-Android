package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12

import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.CoroutineTestRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AndroidVersionRepositoryTest {

    @get: Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    private var insertedIntoDb = false

    @Before
    fun setUp() {
        insertedIntoDb = false
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `getLocalAndroidVersions() should return android versions from database`() =
        coroutineTestRule.runBlockingTest {
            val fakeDatabase = createFakeDatabase()

            val repository = AndroidVersionRepository(fakeDatabase)
            assertEquals(mockAndroidVersions, repository.getLocalAndroidVersions())
        }

    @Test
    fun `loadRecentAndroidVersions() should return android versions from network`() =
        coroutineTestRule.runBlockingTest {
            val fakeDatabase = createFakeDatabase()
            val fakeApi = createFakeApi()
            val repository = AndroidVersionRepository(
                fakeDatabase,
                coroutineTestRule.testDispatcher,
                mockApi = fakeApi
            )
            assertEquals(mockAndroidVersions, repository.loadRemoteAndroidVersions())
        }

    @Test
    fun `loadRecentAndroidVersions() should continue to load and store android versions when calling scope gets cancelled`() =
        coroutineTestRule.runBlockingTest {
            val fakeDatabase = createFakeDatabase()
            val fakeApi = createFakeApi()
            val repository = AndroidVersionRepository(
                fakeDatabase,
                coroutineTestRule.testDispatcher,
                mockApi = fakeApi
            )
            val testScope = TestCoroutineScope(Job())

            testScope.launch {
                val loadedVersions = repository.loadRemoteAndroidVersions()
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

            override suspend fun getAndroidVersionFeatures(apiVersion: Int): VersionFeatures {
                TODO("Not yet implemented")
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