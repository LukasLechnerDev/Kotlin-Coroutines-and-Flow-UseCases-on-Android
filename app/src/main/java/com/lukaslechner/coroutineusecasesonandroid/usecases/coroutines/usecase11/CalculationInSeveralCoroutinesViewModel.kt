package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInSeveralCoroutinesViewModel(
    private val factorialCalculator: FactorialCalculator = FactorialCalculator()
) : BaseViewModel<UiState>() {

    fun performCalculation(
        factorialOf: Int,
        numberOfCoroutines: Int,
        dispatcher: CoroutineDispatcher,
        yieldDuringCalculation: Boolean = true
    ) {
        uiState.value = UiState.Loading
        viewModelScope.launch {

            var factorialResult = BigInteger.ZERO
            val computationDuration = measureTimeMillis {
                factorialResult =
                    factorialCalculator.calculateFactorial(
                        factorialOf,
                        numberOfCoroutines,
                        dispatcher,
                        yieldDuringCalculation
                    )
            }

            var resultString = ""
            val stringConversionDuration = measureTimeMillis {
                resultString = convertToString(factorialResult, dispatcher)
            }

            uiState.value =
                UiState.Success(resultString, computationDuration, stringConversionDuration)
        }
    }

    private suspend fun convertToString(
        number: BigInteger,
        dispatcher: CoroutineDispatcher
    ): String =
        withContext(dispatcher) {
            number.toString()
        }
}