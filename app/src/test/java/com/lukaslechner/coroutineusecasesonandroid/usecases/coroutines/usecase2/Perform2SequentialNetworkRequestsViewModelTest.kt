package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class Perform2SequentialNetworkRequestsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get: Rule
    val replaceMainDispatcherRule: ReplaceMainDispatcherRule = ReplaceMainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `should return Success when both network requests are successful`() {
        // TODO
    }

    @Test
    fun `should return Error when first network requests fails`() {
        // TODO
    }

    @Test
    fun `should return Error when second network requests fails`() {
        // TODO
    }

    private fun Perform2SequentialNetworkRequestsViewModel.observe() {
        uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}