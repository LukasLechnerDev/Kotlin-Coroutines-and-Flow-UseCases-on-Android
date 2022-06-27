package com.lukaslechner.coroutineusecasesonandroid.utils

import com.google.gson.Gson
import okhttp3.*

class MockNetworkInterceptor : Interceptor {

    private val mockResponses = mutableListOf<MockResponse>()
    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val mockResponse = findMockResponseInList(request)
            ?: throw RuntimeException("No mock response found for url ${request.url()}. Please define a mock response in your MockApi!")

        removeResponseIfItShouldNotBePersisted(mockResponse)
        simulateNetworkDelay(mockResponse)

        return if (mockResponse.status < 400) {
            createSuccessResponse(mockResponse, request)
        } else {
            createErrorResponse(request)
        }
    }

    private fun findMockResponseInList(request: Request): MockResponse? {
        return mockResponses.find { mockResponse ->
            mockResponse.path == request.url().toString()
        }
    }

    private fun removeResponseIfItShouldNotBePersisted(mockResponse: MockResponse) {
        if (!mockResponse.persist) {
            mockResponses.remove(mockResponse)
        }
    }

    private fun simulateNetworkDelay(mockResponse: MockResponse) {
        Thread.sleep(mockResponse.delayInMs)
    }

    private fun createErrorResponse(request: Request): Response {
        return Response.Builder()
            .code(500)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("Internal Server Error")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    gson.toJson(mapOf("cause" to "not sure"))
                )
            )
            .build()
    }

    private fun createSuccessResponse(
        mockResponse: MockResponse,
        request: Request
    ): Response {
        return Response.Builder()
            .code(mockResponse.status)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    mockResponse.body.invoke()
                )
            )
            .build()
    }

    fun mock(
        path: String,
        body: () -> String,
        status: Int,
        delayInMs: Long = 250,
        persist: Boolean = true
    ) = apply {
        val mockResponse =
            MockResponse(
                path,
                body,
                status,
                delayInMs,
                persist
            )
        mockResponses.add(mockResponse)
    }

    companion object {
        const val INTERNAL_SERVER_ERROR_HTTP_CODE = 500
    }
}

data class MockResponse(
    val path: String,
    val body: () -> String,
    val status: Int,
    val delayInMs: Long,
    val persist: Boolean
)
