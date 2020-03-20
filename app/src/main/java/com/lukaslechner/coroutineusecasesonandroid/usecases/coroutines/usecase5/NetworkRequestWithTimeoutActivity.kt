package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityNetworkrequestwithtimeoutBinding
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5.NetworkRequestWithTimeoutViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.fromHtml
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class NetworkRequestWithTimeoutActivity : BaseActivity() {

    override fun getToolbarTitle() = "Network Request with TimeOut"

    private val binding by lazy { ActivityNetworkrequestwithtimeoutBinding.inflate(layoutInflater) }
    private val viewModel: NetworkRequestWithTimeoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnPerformSingleNetworkRequest.setOnClickListener {
            val timeOut = binding.editTextTimeOut.text.toString().toLongOrNull()
            if (timeOut != null) {
                viewModel.performNetworkRequest(timeOut)
            }
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
        binding.btnPerformSingleNetworkRequest.isEnabled = false
    }

    private fun onSuccess(uiState: UiState.Success) {
        binding.progressBar.visibility = View.GONE
        binding.btnPerformSingleNetworkRequest.isEnabled = true
        val readableVersions = uiState.recentVersions.map { "API ${it.apiVersion}: ${it.name}" }
        binding.textViewResult.text = fromHtml(
            "<b>Recent Android Versions</b><br>${readableVersions.joinToString(separator = "<br>")}"
        )
    }

    private fun onError(uiState: UiState.Error) {
        binding.progressBar.visibility = View.GONE
        binding.btnPerformSingleNetworkRequest.isEnabled = true
        toast(uiState.message)
    }
}