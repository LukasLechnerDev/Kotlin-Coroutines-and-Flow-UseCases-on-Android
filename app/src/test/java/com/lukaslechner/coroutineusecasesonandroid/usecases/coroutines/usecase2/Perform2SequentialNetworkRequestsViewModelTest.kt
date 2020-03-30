package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.fakes.FakeApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.Perform2SequentialNetworkRequestsViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.CoroutineTestRule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException

class Perform2SequentialNetworkRequestsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val coroutineTestRule: CoroutineTestRule = CoroutineTestRule()

    private val receivedUiStates: MutableList<UiState> =
        arrayListOf()

    @Test
    fun `should return Success when both network requests are successful`() =
        coroutineTestRule.runBlockingTest {

            val fakeApi = FakeApi()
            val viewModel =
                Perform2SequentialNetworkRequestsViewModel(
                    fakeApi,
                    coroutineTestRule.testDispatcher
                )
            observeViewModel(viewModel)

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.perform2SequentialNetworkRequest()
            Assert.assertEquals(
                listOf(UiState.Loading),
                receivedUiStates
            )

            fakeApi.sendResponseToGetRecentAndroidVersionsRequest(mockAndroidVersions)
            fakeApi.sendResponseToGetAndroidVersionFeaturesRequest(29, mockVersionFeaturesAndroid10)

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
        coroutineTestRule.runBlockingTest {

            val fakeApi = FakeApi()
            val viewModel =
                Perform2SequentialNetworkRequestsViewModel(
                    fakeApi,
                    coroutineTestRule.testDispatcher
                )
            observeViewModel(viewModel)

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.perform2SequentialNetworkRequest()
            Assert.assertEquals(
                listOf(UiState.Loading),
                receivedUiStates
            )

            fakeApi.sendErrorToGetRecentAndroidVersionsRequest(IOException())

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
        coroutineTestRule.runBlockingTest {

            val fakeApi = FakeApi()
            val viewModel =
                Perform2SequentialNetworkRequestsViewModel(
                    fakeApi,
                    coroutineTestRule.testDispatcher
                )
            observeViewModel(viewModel)

            Assert.assertTrue(receivedUiStates.isEmpty())

            viewModel.perform2SequentialNetworkRequest()
            Assert.assertEquals(
                listOf(UiState.Loading),
                receivedUiStates
            )

            fakeApi.sendResponseToGetRecentAndroidVersionsRequest(mockAndroidVersions)
            fakeApi.sendErrorToGetAndroidVersionFeaturesRequest(29, IOException())

            Assert.assertEquals(
                listOf(
                    UiState.Loading,
                    UiState.Error("Network Request failed")
                ),
                receivedUiStates
            )
        }

    private fun observeViewModel(viewModel: Perform2SequentialNetworkRequestsViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}