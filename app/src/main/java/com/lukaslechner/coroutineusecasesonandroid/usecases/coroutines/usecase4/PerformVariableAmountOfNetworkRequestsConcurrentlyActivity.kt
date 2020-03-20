package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityPerformvariableamountofnetworkrequestsconcurrentlyBinding
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4.PerformVariableAmountOfNetworkRequestsConcurrentlyViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.fromHtml
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class PerformVariableAmountOfNetworkRequestsConcurrentlyActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPerformvariableamountofnetworkrequestsconcurrentlyBinding.inflate(
            layoutInflater
        )
    }

    private val viewModelVariableAmountOf: PerformVariableAmountOfNetworkRequestsConcurrentlyViewModel by viewModels()

    override fun getToolbarTitle() = "Perform variable amount of Network Requests Concurrently"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModelVariableAmountOf.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })

        binding.btnRequestsSequentially.setOnClickListener {
            viewModelVariableAmountOf.performNetworkRequestsSequentially()
        }

        binding.btnRequestsConcurrently.setOnClickListener {
            viewModelVariableAmountOf.performNetworkRequestsConcurrently()
        }

    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                onLoad()
            }
            is UiState.Success -> {
                onSuccess(uiState)
            }
            is UiState.Error -> {
                onError(uiState)
            }
        }
    }

    private fun onLoad() {
        binding.progressBar.visibility = View.VISIBLE
        binding.textViewResult.text = ""
    }

    private fun onSuccess(uiState: UiState.Success) {
        binding.progressBar.visibility = View.GONE

        val versionFeatures = uiState.versionFeatures

        val versionFeaturesString = versionFeatures.map {
            "<b>New Features of ${it.androidVersion.name} </b> <br> ${it.features.joinToString(
                prefix = "- ",
                separator = "<br>- "
            )}"
        }.joinToString(separator = "<br><br>")

        binding.textViewResult.text = fromHtml(versionFeaturesString)
    }

    private fun onError(uiState: UiState.Error) {
        binding.progressBar.visibility = View.GONE
        toast(uiState.message)
    }
}