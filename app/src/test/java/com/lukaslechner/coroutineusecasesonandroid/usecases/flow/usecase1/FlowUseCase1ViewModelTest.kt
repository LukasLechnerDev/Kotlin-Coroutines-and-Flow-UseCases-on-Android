package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class FlowUseCase1ViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `should return success UI States with values of fakeDataSource`() = runTest {

        val fakeDataSource = FakeStockPriceDataSource()
        val viewModel = FlowUseCase1ViewModel(fakeDataSource)
        observeViewModel(viewModel)

        assertEquals(UiState.Loading, receivedUiStates[0])
        assertEquals(1, receivedUiStates.size)

        fakeDataSource.emit(listOf(googleStock, appleStock))
        assertEquals(UiState.Success(listOf(googleStock, appleStock)), receivedUiStates[1])
        assertEquals(2, receivedUiStates.size)

        fakeDataSource.emit(listOf(googleStock))
        assertEquals(UiState.Success(listOf(googleStock)), receivedUiStates[2])
        assertEquals(3, receivedUiStates.size)
    }

    private fun observeViewModel(viewModel: FlowUseCase1ViewModel) {
        viewModel.currentStockPriceAsLiveData.observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}