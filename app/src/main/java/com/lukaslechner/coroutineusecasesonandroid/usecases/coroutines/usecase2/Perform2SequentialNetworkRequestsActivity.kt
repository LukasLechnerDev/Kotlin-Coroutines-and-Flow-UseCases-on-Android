package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityPerform2sequentialnetworkrequestsBinding
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.Perform2SequentialNetworkRequestsViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.fromHtml
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class Perform2SequentialNetworkRequestsActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPerform2sequentialnetworkrequestsBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: Perform2SequentialNetworkRequestsViewModel by viewModels()

    override fun getToolbarTitle() = "Perform 2 Sequential Network Requests"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnRequestsSequentially.setOnClickListener {
            viewModel.perform2SequentialNetworkRequest()
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
        binding.textViewResult.text = fromHtml(
            "<b>Features of most recent Android Version \" ${uiState.versionFeatures.androidVersion.name} \"</b><br>" +
                    uiState.versionFeatures.features.joinToString(
                        prefix = "- ",
                        separator = "<br>- "
                    )
        )
    }

    private fun onError(uiState: UiState.Error) {
        binding.progressBar.visibility = View.GONE
        toast(uiState.message)
    }
}