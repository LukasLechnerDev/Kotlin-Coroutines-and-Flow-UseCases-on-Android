package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1.appleStock
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1.googleStock
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FlowUseCase4ViewModelTest {

    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Test
    fun `should collect loading and success ui states on successful emissions`() = runTest {

        val fakeStockPriceDataSource = FakeStockPriceDataSource()
        val viewModel = FlowUseCase4ViewModel(fakeStockPriceDataSource)

        val collectJob =
            launch(UnconfinedTestDispatcher()) {
                viewModel.currentStockPriceAsFlow.collect()
            }

        assertEquals(
            UiState.Loading,
            viewModel.currentStockPriceAsFlow.value
        )

        fakeStockPriceDataSource.emit(
            listOf(
                googleStock,
                appleStock
            )
        )

        assertEquals(
            UiState.Success(
                listOf(
                    googleStock,
                    appleStock
                )
            ),
            viewModel.currentStockPriceAsFlow.value
        )

        fakeStockPriceDataSource.emit(listOf(googleStock))

        assertEquals(
            UiState.Success(
                listOf(
                    googleStock
                )
            ),
            viewModel.currentStockPriceAsFlow.value
        )

        collectJob.cancel()
    }
}