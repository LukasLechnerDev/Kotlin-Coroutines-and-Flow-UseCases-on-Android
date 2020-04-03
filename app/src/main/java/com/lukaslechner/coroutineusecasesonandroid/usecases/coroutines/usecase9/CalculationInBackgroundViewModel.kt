package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase9

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import timber.log.Timber
import java.math.BigInteger

class CalculationInBackgroundViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private var calculationJob: Job? = null

    fun performCalculation(factorialOf: Int) {
        calculationJob = viewModelScope.launch {
            uiState.value = UiState.Loading
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
            Timber.d("Finished Calculation")
            factorial
        }

    private suspend fun convertToString(number: BigInteger): String =
        withContext(defaultDispatcher) {
            number.toString()
        }

    fun uiState(): LiveData<UiState> = uiState

    fun cancelCalculation() {
        calculationJob?.cancel()
        Timber.d("Canceled Calculation")
    }

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