package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.math.BigInteger

class CalculationInMultipleBackgroundThreadsViewModel(
    private val factorialCalculator: FactorialCalculator = FactorialCalculator()
) : ViewModel() {

    fun performCalculation(factorialOf: Int, numberOfThreads: Int) {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            val factorialResult =
                factorialCalculator.calculateFactorial(factorialOf, numberOfThreads)
            uiState.value = UiState.Success(factorialResult)
        }
    }

    fun uiState(): LiveData<UiState> = uiState
    private val uiState: MutableLiveData<UiState> = MutableLiveData()

    sealed class UiState {
        object Loading : UiState()
        data class Success(val result: BigInteger) : UiState()
        data class Error(val message: String) : UiState()
    }
}