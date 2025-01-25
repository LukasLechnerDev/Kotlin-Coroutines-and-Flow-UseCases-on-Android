package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {

        uiState.value = UiState.Loading

        // study inside documentation of this later
        //also taught later in course
        viewModelScope.launch {
            //just looks like normal fxn call
            // AT first it looks like we are synchronously calling 2 functions and blocking the main thread
            // but actually that is not the case, main thread is not blocked
            //Suspend fxns. when implemented correctly are non-blocking
            try {
                val recentVersions = mockApi.getRecentAndroidVersions()
                val featuresOfLatestVersion = mockApi
                    .getAndroidVersionFeatures(
                        recentVersions.last().apiLevel
                    )
                uiState.value = UiState.Success(featuresOfLatestVersion)
            } catch (e: Exception) {
                uiState.value = UiState.Error("Network request failed")
            }
            // exception hanndling here is also easy
        }



    }
}