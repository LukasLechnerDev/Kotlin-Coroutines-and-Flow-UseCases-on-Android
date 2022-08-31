package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.FlowMockApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FlowUseCase1ViewModel(
    private val mockApi: FlowMockApi = mockApi()
) : BaseViewModel<UiState>() {

    val bitcoinPrice = MutableLiveData<UiState>(UiState.Loading)

    fun whileTrueInCoroutine() {
        viewModelScope.launch {
            while (true) {
                val currentBitcoinPrice = mockApi.getCurrentBitcoinPrice()
                bitcoinPrice.value = UiState.Success(currentBitcoinPrice)
                delay(5_000)
            }
        }
    }
}