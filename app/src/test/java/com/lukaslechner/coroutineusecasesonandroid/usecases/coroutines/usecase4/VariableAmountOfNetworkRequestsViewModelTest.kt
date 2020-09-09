package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesOreo
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesPie
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class VariableAmountOfNetworkRequestsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performNetworkRequestsSequentially() should return Success UiState on successful network requests after 4000ms`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsSequentially()

            val forwardedTime = advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ),
                receivedUiStates
            )

            Assert.assertEquals(
                4000,
                forwardedTime
            )
        }

    @Test
    fun `performNetworkRequestsSequentially() should return Error UiState on unsuccessful recent-android-versions network request`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeVersionsErrorApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsSequentially()

            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network Request failed")
                ),
                receivedUiStates
            )
        }

    @Test
    fun `performNetworkRequestsSequentially() should return Error UiState on unsuccessful android-version-features network request`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeFeaturesErrorApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsSequentially()

            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network Request failed")
                ),
                receivedUiStates
            )
        }

    @Test
    fun `performNetworkRequestsConcurrently() should return Error UiState on successful network requests after 2000ms`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeSuccessApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsConcurrently()

            val forwardedTime = advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ),
                receivedUiStates
            )

            Assert.assertEquals(
                2000,
                forwardedTime
            )
        }

    @Test
    fun `performNetworkRequestsConcurrently() should return Error UiState on unsuccessful recent-android-versions network request`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeVersionsErrorApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsSequentially()

            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network Request failed")
                ),
                receivedUiStates
            )
        }

    @Test
    fun `performNetworkRequestsConcurrently() should return Error UiState on unsuccessful android-version-features network request`() =
        mainCoroutineScopeRule.runBlockingTest {
            val responseDelay = 1000L
            val fakeApi = FakeFeaturesErrorApi(responseDelay)
            val viewModel = VariableAmountOfNetworkRequestsViewModel(fakeApi)
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.performNetworkRequestsSequentially()

            advanceUntilIdle()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network Request failed")
                ),
                receivedUiStates
            )
        }

    private fun VariableAmountOfNetworkRequestsViewModel.observe() {
        uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}