package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.rx

import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class SequentialNetworkRequestsRxViewModel(
    private val mockApi: RxMockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val disposables = CompositeDisposable()

    fun perform2SequentialNetworkRequest() {

        //use flatMap to chain the two network requests
        // .subscribeOn(Schedulers.io()) is used to perform the network requests on a background thread
        // .observeOn(AndroidSchedulers.mainThread()) is used to observe the results on the main thread
        mockApi.getRecentAndroidVersions()
            .flatMap {
                val latestVersion = it.last()
                mockApi.getAndroidVersionFeatures(latestVersion.apiLevel)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy  (
                onSuccess = {
                    uiState.value = UiState.Success(it)
                },
                onError = {
                    uiState.value = UiState.Error("Network request failed")
                }
            )
            .addTo(disposables)

    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}