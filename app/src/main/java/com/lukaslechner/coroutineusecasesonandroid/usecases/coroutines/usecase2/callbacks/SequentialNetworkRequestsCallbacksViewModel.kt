package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks

import androidx.lifecycle.AndroidViewModel
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SequentialNetworkRequestsCallbacksViewModel(
    private val mockApi: CallbackMockApi = mockApi()
) : BaseViewModel<UiState>() {

    var getVersions: Call<List<AndroidVersion>>? = null;
    var getFeaturesCall: Call<VersionFeatures>? = null;

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        getVersions = mockApi.getRecentAndroidVersions()
        getVersions!!.enqueue(
            object : Callback<List<AndroidVersion>>{
                override fun onResponse(
                    call: Call<List<AndroidVersion>>,
                    response: Response<List<AndroidVersion>>
                ) {
                    if(response.isSuccessful) { //status code >= 200 and  <300
                        val latestVersion = response.body()!!.last()

                        //Perform Second Call only if first call was successful.

                        getFeaturesCall = mockApi.getAndroidVersionFeatures(latestVersion.apiLevel)
                        getFeaturesCall!!.enqueue(
                                object : Callback<VersionFeatures> {
                                    override fun onResponse(
                                        call: Call<VersionFeatures>,
                                        response: Response<VersionFeatures>
                                    ) {
                                        if(response.isSuccessful) {
                                            val featuresOfMostRecentVersion = response.body()!!
                                            uiState.value = UiState.Success(
                                                featuresOfMostRecentVersion
                                            )
                                        } else {
                                            uiState.value = UiState.Error("Network request failed")
                                        }
                                    }

                                    override fun onFailure(call: Call<VersionFeatures>, t: Throwable) {
                                        uiState.value = UiState.Error("Something went wrong")
                                    }
                                }

                            )
                    } else {
                        uiState.value = UiState.Error("Network request failed")
                    }
                }

                override fun onFailure(call: Call<List<AndroidVersion>>, t: Throwable) {
                    uiState.value = UiState.Error("Something went wrong")
                }
            }
        )

        //since the callbacks are holding the reference to viewModel, it wont get garbage collected when user has left the screen.

    }

    override fun onCleared() {
        //this cant access callbacks of prev fxn, so we make them top level.

        super.onCleared()

        getVersions?.cancel()
        getFeaturesCall?.cancel()
    }
}