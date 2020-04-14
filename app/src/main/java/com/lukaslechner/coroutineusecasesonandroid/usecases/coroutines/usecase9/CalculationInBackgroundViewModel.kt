package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase9

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger

class CalculationInBackgroundViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel<UiState>() {

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val computationStart = System.currentTimeMillis()
                val result = calculateFactorialOf(factorialOf)
                val computationDuration = System.currentTimeMillis() - computationStart

                val stringConversionStart = System.currentTimeMillis()
                val resultString = convertToString(result)
                val stringConversionDuration = System.currentTimeMillis() - stringConversionStart

                uiState.value =
                    UiState.Success(resultString, computationDuration, stringConversionDuration)
            } catch (exception: Exception) {
                UiState.Error("Error while calculating result")
            }
        }
    }

    // factorial of n (n!) = 1 * 2 * 3 * 4 * ... * n
    private suspend fun calculateFactorialOf(number: Int): BigInteger =
        withContext(defaultDispatcher) {
            var factorial = BigInteger.ONE
            for (i in 1..number) {
                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            factorial
        }

    private suspend fun convertToString(number: BigInteger): String =
        withContext(defaultDispatcher) {
            number.toString()
        }
}