package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SequentialNetworkRequestsCallbacksViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val receivedUiStates: MutableList<UiState> =
        arrayListOf()

    @Test
    fun `should return Success when both network requests are successful`() {
        val fakeApi = FakeSuccessApi()
        val viewModel =
            SequentialNetworkRequestsCallbacksViewModel(
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
    fun `should return Error when first network request (recent-android-versions) fails`() {
        val fakeApi = FakeVersionsErrorApi()
        val viewModel =
            SequentialNetworkRequestsCallbacksViewModel(
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
    fun `should return Error when second network requests (android-version-features) fails`() {
        val fakeApi = FakeFeaturesErrorApi()
        val viewModel =
            SequentialNetworkRequestsCallbacksViewModel(
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

    private fun SequentialNetworkRequestsCallbacksViewModel.observe() {
        uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }

}