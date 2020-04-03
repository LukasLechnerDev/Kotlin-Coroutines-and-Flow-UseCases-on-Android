package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger

class CalculationInMultipleBackgroundThreadsViewModel(
    private val factorialCalculator: FactorialCalculator = FactorialCalculator(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    fun performCalculation(factorialOf: Int, numberOfThreads: Int) {
        viewModelScope.launch {
            uiState.value = UiState.Loading

            val computationStart = System.currentTimeMillis()
            val factorialResult =
                factorialCalculator.calculateFactorial(factorialOf, numberOfThreads)
            val computationDuration = System.currentTimeMillis() - computationStart

            val stringConversionStart = System.currentTimeMillis()
            val resultString = convertToString(factorialResult)
            val stringConversionDuration = System.currentTimeMillis() - stringConversionStart

            uiState.value =
                UiState.Success(resultString, computationDuration, stringConversionDuration)
        }
    }

    private suspend fun convertToString(number: BigInteger): String =
        withContext(defaultDispatcher) {
            number.toString()
        }

    fun uiState(): LiveData<UiState> = uiState
    private val uiState: MutableLiveData<UiState> = MutableLiveData()

    sealed class UiState {
        object Loading : UiState()
        data class Success(
            val result: String,
            val computationDuration: Long,
            val stringConversionDuration: Long
        ) : UiState()
        data class Error(val message: String) : UiState()
    }
}