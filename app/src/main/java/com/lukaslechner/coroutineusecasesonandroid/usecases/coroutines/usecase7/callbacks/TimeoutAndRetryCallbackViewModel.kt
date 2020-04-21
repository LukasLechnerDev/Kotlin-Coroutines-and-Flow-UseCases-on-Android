package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7.callbacks

import android.os.Handler
import android.os.Looper
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class TimeoutAndRetryCallbackViewModel(api: CallbackMockApi = mockApi()) :
    BaseViewModel<UiState>() {

    private val totalAttempts = 3

    private var oreoFeaturesRequestAttemptNumber = 0
    private var pieFeaturesRequestAttemptNumber = 0

    private val shouldRetryOreoFeaturesRequest: Boolean
        get() = oreoFeaturesRequestAttemptNumber < totalAttempts

    private val shouldRetryPieFeaturesRequest: Boolean
        get() = pieFeaturesRequestAttemptNumber < totalAttempts

    private val timeout = 1000L

    private val oreoFeaturesTimeoutHandler = Handler(Looper.getMainLooper())
    private val pieFeaturesTimeoutHandler = Handler(Looper.getMainLooper())

    private var oreoFeaturesCall = api.getAndroidVersionFeatures(27)
    private var pieFeaturesCall = api.getAndroidVersionFeatures(28)

    private var oreoFeaturesResult: VersionFeatures? = null
    private var pieFeaturesResult: VersionFeatures? = null

    private val oreoFeaturesReceived: Boolean
        get() = oreoFeaturesResult != null

    private val pieFeaturesReceived: Boolean
        get() = pieFeaturesResult != null

    fun performNetworkRequest() {
        uiState.value = UiState.Loading

        getOreoFeatures()
        getPieFeatures()
    }

    private fun getOreoFeatures() {
        oreoFeaturesRequestAttemptNumber++
        Timber.d("Start get oreo features request")
        oreoFeaturesCall = oreoFeaturesCall.clone()
        oreoFeaturesCall.enqueue(object : Callback<VersionFeatures> {
            override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                Timber.e("Get oreo features onFailure() entered: $t")
                stopOreoFeaturesTimeoutHandler()
                postErrorOrRetryGetOreoFeatures()
            }

            override fun onResponse(
                call: Call<VersionFeatures>,
                response: Response<VersionFeatures>
            ) {
                stopOreoFeaturesTimeoutHandler()
                if (!response.isSuccessful) {
                    Timber.e("Get oreo features request was unsuccessful")
                    postErrorOrRetryGetOreoFeatures()
                    return
                } else {
                    Timber.d("successful oreo response received.")
                }
                oreoFeaturesResult = response.body()
                maybeNotifyView()
            }
        })
        setOreoFeaturesTimeout()
    }

    private fun setOreoFeaturesTimeout() {
        val retryRunnable = Runnable {
            Timber.e("Timeout for getting oreo features")
            if (!oreoFeaturesReceived) {
                oreoFeaturesCall.cancel()
            }
        }
        Timber.d("start oreo Features Timeout Handler")
        oreoFeaturesTimeoutHandler.postDelayed(retryRunnable, timeout)
    }

    private fun postErrorOrRetryGetOreoFeatures() {
        if (shouldRetryOreoFeaturesRequest) {
            getOreoFeatures()
        } else {
            uiState.value = UiState.Error("Network Request failed.")
            cancelNetworkRequests()
        }
    }

    private fun getPieFeatures() {
        pieFeaturesRequestAttemptNumber++
        Timber.d("Start get pie features request")
        pieFeaturesCall = pieFeaturesCall.clone()
        pieFeaturesCall.enqueue(object : Callback<VersionFeatures> {
            override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                Timber.e("Get pie features onFailure() entered: $t")
                stopPieFeaturesTimeoutHandler()
                postErrorOrRetryGetPieFeatures()
            }

            override fun onResponse(
                call: Call<VersionFeatures>,
                response: Response<VersionFeatures>
            ) {
                stopPieFeaturesTimeoutHandler()

                if (!response.isSuccessful) {
                    Timber.e("Get pie features request was unsuccessful")
                    postErrorOrRetryGetPieFeatures()
                    return
                } else {
                    Timber.d("successful pie response received.")
                }

                pieFeaturesResult = response.body()
                maybeNotifyView()
            }
        })
        setPieFeaturesTimeout()
    }

    private fun stopPieFeaturesTimeoutHandler() {
        Timber.d("stopping pie Features Timeout Handler")
        pieFeaturesTimeoutHandler.removeCallbacksAndMessages(null)
    }

    private fun setPieFeaturesTimeout() {
        val retryRunnable = Runnable {
            Timber.e("Timeout for getting pie features")
            if (!pieFeaturesReceived) {
                pieFeaturesCall.cancel()
            }
        }
        Timber.d("start pie Features Timeout Handler")
        pieFeaturesTimeoutHandler.postDelayed(retryRunnable, timeout)
    }

    private fun postErrorOrRetryGetPieFeatures() {
        if (shouldRetryPieFeaturesRequest) {
            getPieFeatures()
        } else {
            uiState.value = UiState.Error("Network Request failed.")
            cancelNetworkRequests()
        }
    }

    private fun maybeNotifyView() {
        if (oreoFeaturesReceived && pieFeaturesReceived) {
            uiState.value = UiState.Success(listOf(oreoFeaturesResult!!, pieFeaturesResult!!))
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelNetworkRequests()
        stopOreoFeaturesTimeoutHandler()
        stopPieFeaturesTimeoutHandler()
    }

    private fun stopOreoFeaturesTimeoutHandler() {
        Timber.d("stopping oreo Features Timeout Handler")
        oreoFeaturesTimeoutHandler.removeCallbacksAndMessages(null)
    }

    private fun cancelNetworkRequests() {
        oreoFeaturesCall.cancel()
        pieFeaturesCall.cancel()
    }
}