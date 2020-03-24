package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.mock.createMockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class PerformSingleNetworkRequestViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher)

    private val receivedUiStates: MutableList<PerformSingleNetworkRequestViewModel.UiState> =
        arrayListOf()

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        receivedUiStates.clear()
    }

    @Test
    fun `should return Success when network request is successful`() = runBlocking {

        val mockApi = createMockApi(
            MockNetworkInterceptor()
                .mock(
                    "http://localhost/recent-android-versions",
                    Gson().toJson(mockAndroidVersions),
                    200,
                    1500
                )
        )

        val viewModel = PerformSingleNetworkRequestViewModel(mockApi)
        observeViewModel(viewModel)

        assertTrue(receivedUiStates.isNullOrEmpty())

        viewModel.performSingleNetworkRequest()

        delay(2000)
        assertEquals(2, receivedUiStates.size)
        assertEquals(PerformSingleNetworkRequestViewModel.UiState.Loading, receivedUiStates.first())
        assertEquals(
            PerformSingleNetworkRequestViewModel.UiState.Success(mockAndroidVersions),
            receivedUiStates[1]
        )
    }

    @Test
    fun `should return Error when network request fails`() = runBlocking {
        val mockApi = createMockApi(
            MockNetworkInterceptor()
                .mock(
                    "http://localhost/recent-android-versions",
                    Gson().toJson(mockAndroidVersions),
                    500,
                    1500
                )
        )

        val viewModel = PerformSingleNetworkRequestViewModel(mockApi)
        observeViewModel(viewModel)

        assertTrue(receivedUiStates.isNullOrEmpty())

        viewModel.performSingleNetworkRequest()

        delay(2000)
        assertEquals(2, receivedUiStates.size)
        assertEquals(PerformSingleNetworkRequestViewModel.UiState.Loading, receivedUiStates.first())
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