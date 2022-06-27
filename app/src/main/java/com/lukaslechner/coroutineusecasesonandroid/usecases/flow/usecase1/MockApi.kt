package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import com.google.gson.Gson
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.createFlowMockApi
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.fakeCurrentGoogleStockPrice
import com.lukaslechner.coroutineusecasesonandroid.utils.MockNetworkInterceptor

fun mockApi() =
    createFlowMockApi(
        MockNetworkInterceptor()
            .mock(
                path = "http://localhost/current-google-stock-price",
                body = { Gson().toJson(fakeCurrentGoogleStockPrice()) },
                status = 200,
                delayInMs = 100,
            )
    )