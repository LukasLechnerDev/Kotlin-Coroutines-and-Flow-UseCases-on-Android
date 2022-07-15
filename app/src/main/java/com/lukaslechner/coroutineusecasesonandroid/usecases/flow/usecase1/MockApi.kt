package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.createFlowMockApi
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.fakeCurrentBitcoinPrice
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor

fun mockApi() =
    createFlowMockApi(
        MockNetworkInterceptor()
            .mock(
                path = "http://localhost/current-bitcoin-price",
                body = { Gson().toJson(fakeCurrentBitcoinPrice()) },
                status = 200,
                delayInMs = 1500,
            )
    )