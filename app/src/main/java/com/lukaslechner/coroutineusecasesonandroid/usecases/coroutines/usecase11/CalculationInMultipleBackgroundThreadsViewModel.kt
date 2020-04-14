package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger

class CalculationInMultipleBackgroundThreadsViewModel(
    private val factorialCalculator: FactorialCalculator = FactorialCalculator(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel<UiState>() {

    fun performCalculation(factorialOf: Int, numberOfThreads: Int) {
        uiState.value = UiState.Loading
        viewModelScope.launch {

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
}