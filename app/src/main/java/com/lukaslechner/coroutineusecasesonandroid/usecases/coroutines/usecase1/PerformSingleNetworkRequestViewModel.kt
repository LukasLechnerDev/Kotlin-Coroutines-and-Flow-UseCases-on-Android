package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        viewModelScope.launch {
            uiState.value = UiState.Loading
            uiState.value =
                runCatching {
                    UiState.Success(mockApi.getRecentAndroidVersions())
                }.getOrElse { throwable ->
                    when (throwable) {
                        is HttpException -> UiState.Error("${throwable.code()} : ${throwable.message()}")
                        else -> UiState.Error(throwable.message.toString())
                    }
                }
        }
    }
}