package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class Perform2SequentialNetworkRequestsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val mainCoroutineScopeRule: MainCoroutineScopeRule = MainCoroutineScopeRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `should return Success when both network requests are successful`() =
        mainCoroutineScopeRule.runBlockingTest {
            val fakeApi = FakeSuccessApi()
            val viewModel =
                Perform2SequentialNetworkRequestsViewModel(
                    fakeApi
                )
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.perform2SequentialNetworkRequest()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Success(mockVersionFeaturesAndroid10)
                ),
                receivedUiStates
            )
        }

    @Test
    fun `should return Error when first network requests fails`() =
        mainCoroutineScopeRule.runBlockingTest {

            val fakeApi = FakeVersionsErrorApi()
            val viewModel =
                Perform2SequentialNetworkRequestsViewModel(
                    fakeApi
                )
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.perform2SequentialNetworkRequest()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network Request failed")
                ),
                receivedUiStates
            )
        }

    @Test
    fun `should return Error when second network requests fails`() =
        mainCoroutineScopeRule.runBlockingTest {

            val fakeApi = FakeFeaturesErrorApi()
            val viewModel =
                Perform2SequentialNetworkRequestsViewModel(
                    fakeApi
                )
            viewModel.observe()

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.perform2SequentialNetworkRequest()

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network Request failed")
                ),
                receivedUiStates
            )
        }

    private fun Perform2SequentialNetworkRequestsViewModel.observe() {
        uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}