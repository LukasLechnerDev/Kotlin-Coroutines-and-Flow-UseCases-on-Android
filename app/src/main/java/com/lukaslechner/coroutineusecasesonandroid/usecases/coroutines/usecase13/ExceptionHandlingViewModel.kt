package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import timber.log.Timber

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun handleExceptionWithTryCatch() {

    }

    fun handleWithCoroutineExceptionHandler() {

    }

    fun showResultsEvenIfChildCoroutineFails() {

    }
}