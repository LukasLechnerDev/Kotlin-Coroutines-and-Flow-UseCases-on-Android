package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class RetryNetworkRequestViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performSingleNetworkRequest() should return Success UiState on successful network response`() =
        coroutineTestRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = RetryNetworkRequestViewModel(fakeApi).apply {
                observe()
            }

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequest()

            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(mockAndroidVersions)
                ),
                receivedUiStates
            )
        }

    @Test
    fun `performSingleNetworkRequest() should retry network request two times`() =
        coroutineTestRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessOnThirdAttemptApi(responseDelay)
            val viewModel = RetryNetworkRequestViewModel(fakeApi).apply {
                observe()
            }

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequest()

            val elapsedTime = advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(mockAndroidVersions)
                ),
                receivedUiStates
            )

            Assert.assertEquals(
                3,
                fakeApi.requestCount
            )

            // 3*1000 (Request delays) + 100 (initial delay) + 200 (second delay)
            Assert.assertEquals(
                3300,
                elapsedTime
            )
        }

    @Test
    fun `performSingleNetworkRequest() should return Error UiState on 3 unsuccessful network responses`() =
        coroutineTestRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeVersionsErrorApi(responseDelay)
            val viewModel = RetryNetworkRequestViewModel(fakeApi).apply {
                observe()
            }

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequest()

            val elapsedTime = advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network Request failed")
                ),
                receivedUiStates
            )

            Assert.assertEquals(
                3,
                fakeApi.requestCount
            )

            // 3*1000 response delays + 100 (initial delay) + 200 (second delay)
            Assert.assertEquals(
                3300,
                elapsedTime
            )
        }

    private fun RetryNetworkRequestViewModel.observe() {
        uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}