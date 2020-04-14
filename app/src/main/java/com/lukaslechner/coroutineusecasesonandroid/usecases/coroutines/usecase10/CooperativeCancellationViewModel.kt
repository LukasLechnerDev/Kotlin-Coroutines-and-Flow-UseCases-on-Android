package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.math.BigInteger

class CooperativeCancellationViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private var calculationJob: Job? = null

    fun performCalculation(factorialOf: Int) {
        uiState.value = UiState.Loading
        calculationJob = viewModelScope.launch {
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
                uiState.value = if (exception is CancellationException) {
                    UiState.Error("Calculation was cancelled")
                } else {
                    UiState.Error("Error while calculating result")
                }
            }
        }
    }

    // factorial of n (n!) = 1 * 2 * 3 * 4 * ... * n
    private suspend fun calculateFactorialOf(number: Int): BigInteger =
        withContext(defaultDispatcher) {
            var factorial = BigInteger.ONE
            for (i in 1..number) {

                // yield enables cooperative cancellations
                // alternatives:
                // - ensureActive()
                // - isActive() - possible to do clean up tasks with
                yield()

                factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
            }
            factorial
        }

    private suspend fun convertToString(number: BigInteger): String =
        withContext(defaultDispatcher) {
            number.toString()
        }

    fun uiState(): LiveData<UiState> = uiState

    fun cancelCalculation() {
        calculationJob?.cancel()
    }

    private val uiState: MutableLiveData<UiState> = MutableLiveData()
}