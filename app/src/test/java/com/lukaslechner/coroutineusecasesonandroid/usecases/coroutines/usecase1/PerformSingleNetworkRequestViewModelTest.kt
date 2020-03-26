package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.fakes.FakeApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException

@ExperimentalCoroutinesApi
class PerformSingleNetworkRequestViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private val receivedUiStates: MutableList<PerformSingleNetworkRequestViewModel.UiState> =
        arrayListOf()

    @Before
    fun setUp() {
        // TODO: Move to JUnit Rule
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()

        // looks for Coroutine leaks
        testDispatcher.cleanupTestCoroutines()

        receivedUiStates.clear()
    }

    @Test
    fun `should return Success when network request is successful`() = runBlockingTest {

        val simpleFakeApi = FakeApi()
        val viewModel = PerformSingleNetworkRequestViewModel(simpleFakeApi, testDispatcher)
        observeViewModel(viewModel)

        assertTrue(receivedUiStates.isEmpty())

        viewModel.performSingleNetworkRequest()
        assertEquals(1, receivedUiStates.size)
        assertEquals(PerformSingleNetworkRequestViewModel.UiState.Loading, receivedUiStates.first())

        simpleFakeApi.sendResponseToGetRecentAndroidVersionsRequest(mockAndroidVersions)
        assertEquals(2, receivedUiStates.size)
        assertEquals(
            PerformSingleNetworkRequestViewModel.UiState.Success(mockAndroidVersions),
            receivedUiStates[1]
        )
    }

    @Test
    fun `should return Error when network request fails`() = runBlockingTest {

        val simpleFakeApi = FakeApi()

        val viewModel = PerformSingleNetworkRequestViewModel(simpleFakeApi, testDispatcher)
        observeViewModel(viewModel)

        assertTrue(receivedUiStates.isEmpty())

        viewModel.performSingleNetworkRequest()
        assertEquals(1, receivedUiStates.size)
        assertEquals(PerformSingleNetworkRequestViewModel.UiState.Loading, receivedUiStates.first())

        simpleFakeApi.sendErrorToGetRecentAndroidVersionsRequest(IOException())
        assertEquals(2, receivedUiStates.size)
        assertEquals(
            PerformSingleNetworkRequestViewModel.UiState.Error("Network Request failed"),
            receivedUiStates[1]
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