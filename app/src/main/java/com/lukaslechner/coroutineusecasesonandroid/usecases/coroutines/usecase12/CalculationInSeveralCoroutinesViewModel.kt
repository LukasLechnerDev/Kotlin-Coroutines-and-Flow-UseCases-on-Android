package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.math.BigInteger
import kotlin.system.measureTimeMillis

class CalculationInSeveralCoroutinesViewModel(
    private val factorialCalculator: FactorialCalculator = FactorialCalculator(),
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : BaseViewModel<UiState>() {

    fun performCalculation(
        factorialOf: Int,
        numberOfCoroutines: Int
    ) {
        uiState.value = UiState.Loading

        var factorialResult = BigInteger.ZERO
        val computationDuration = measureTimeMillis {
            factorialResult =
                factorialCalculator.calculateFactorial(
                    factorialOf,
                    numberOfCoroutines
                )
        }

        var resultString = ""
        val stringConversionDuration = measureTimeMillis {
            resultString = convertToString(factorialResult)
        }

        uiState.value =
            UiState.Success(resultString, computationDuration, stringConversionDuration)
    }

    // TODO: execute on background thread
    private fun convertToString(number: BigInteger): String = number.toString()
}