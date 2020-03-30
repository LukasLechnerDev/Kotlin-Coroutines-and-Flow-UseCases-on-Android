package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.fakes.FakeApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1.PerformSingleNetworkRequestViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException

@ExperimentalCoroutinesApi
class PerformSingleNetworkRequestViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    private val receivedUiStates: MutableList<UiState> =
        arrayListOf()

    @Test
    fun `should return Success when network request is successful`() =
        coroutineTestRule.runBlockingTest {

            val fakeApi = FakeApi()
            val viewModel =
                PerformSingleNetworkRequestViewModel(fakeApi, coroutineTestRule.testDispatcher)
            observeViewModel(viewModel)

            assertTrue(receivedUiStates.isEmpty())

            viewModel.performSingleNetworkRequest()
            assertEquals(listOf(UiState.Loading), receivedUiStates)

            fakeApi.sendResponseToGetRecentAndroidVersionsRequest(mockAndroidVersions)
            assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(mockAndroidVersions)
                ),
                receivedUiStates
            )
        }

    @Test
    fun `should return Error when network request fails`() = coroutineTestRule.runBlockingTest {

        val fakeApi = FakeApi()

        val viewModel =
            PerformSingleNetworkRequestViewModel(fakeApi, coroutineTestRule.testDispatcher)
        observeViewModel(viewModel)

        assertTrue(receivedUiStates.isEmpty())

        viewModel.performSingleNetworkRequest()
        assertEquals(listOf(UiState.Loading), receivedUiStates)

        fakeApi.sendErrorToGetRecentAndroidVersionsRequest(IOException())
        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network Request failed")
            ),
            receivedUiStates
        )
    }

    private fun observeViewModel(viewModel: PerformSingleNetworkRequestViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}