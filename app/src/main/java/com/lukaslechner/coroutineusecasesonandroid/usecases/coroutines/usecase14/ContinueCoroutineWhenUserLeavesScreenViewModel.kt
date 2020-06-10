package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel

class ContinueCoroutineWhenUserLeavesScreenViewModel(
    private var repository: AndroidVersionRepository
) : BaseViewModel<UiState>() {

    fun loadData() {

    }

    fun clearDatabase() {

    }
}

sealed class DataSource(val name: String) {
    object Database : DataSource("Database")
    object Network : DataSource("Network")
}