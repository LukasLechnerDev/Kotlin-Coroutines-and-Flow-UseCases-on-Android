package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityPerformnetworkrequestsconcurrentlyBinding
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3.PerformNetworkRequestsConcurrentlyViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.fromHtml
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class PerformNetworkRequestsConcurrentlyActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPerformnetworkrequestsconcurrentlyBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: PerformNetworkRequestsConcurrentlyViewModel by viewModels()
    override fun getToolbarTitle() = "Perform network requests concurrently"

    private var operationStartTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnRequestsSequentially.setOnClickListener {
            operationStartTime = System.currentTimeMillis()
            viewModel.performNetworkRequestsSequentially()
        }
        binding.btnRequestsConcurrently.setOnClickListener {
            operationStartTime = System.currentTimeMillis()
            viewModel.performNetworkRequestsConcurrently()
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
        binding.textViewDuration.text = ""
        binding.textViewResult.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        binding.progressBar.visibility = View.GONE
        val duration = System.currentTimeMillis() - operationStartTime
        binding.textViewDuration.text = getString(R.string.duration, duration)

        val versionFeatures = uiState.versionFeatures
        val versionFeaturesString = versionFeatures.joinToString(separator = "<br><br>") {
            "<b>New Features of ${it.androidVersion.name} </b> <br> ${it.features.joinToString(
                prefix = "- ",
                separator = "<br>- "
            )}"
        }

        binding.textViewResult.text = fromHtml(versionFeaturesString)
    }

    private fun onError(uiState: UiState.Error) {
        binding.progressBar.visibility = View.GONE
        binding.textViewDuration.visibility = View.GONE
        toast(uiState.message)
    }

    private fun enableButtons() {
        binding.btnRequestsSequentially.isEnabled = true
        binding.btnRequestsConcurrently.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnRequestsSequentially.isEnabled = false
        binding.btnRequestsConcurrently.isEnabled = false
    }
}