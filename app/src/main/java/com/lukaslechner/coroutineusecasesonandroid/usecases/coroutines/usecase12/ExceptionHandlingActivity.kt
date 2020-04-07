package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.base.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityExceptionhandlingBinding
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12.ExceptionHandlingViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.fromHtml
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast

class ExceptionHandlingActivity : BaseActivity() {

    private val binding by lazy {
        ActivityExceptionhandlingBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: ExceptionHandlingViewModel by viewModels()
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
        binding.btnExceptionTryCatch.setOnClickListener {
            viewModel.handleExceptionWithTryCatch()
        }
        binding.btnCoroutineExceptionHandler.setOnClickListener {
            viewModel.handleWithCoroutineExceptionHandler()
        }
        binding.btnShowResultsEvenIfChildCoroutineFailsTryCatch.setOnClickListener {
            viewModel.showResultsEvenIfChildCoroutineFails()
        }
        binding.btnShowResultsEvenIfChildCoroutineFailsRunCatching.setOnClickListener {
            viewModel.showResultsEvenIfChildCoroutineFailsRunCatching()
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
        operationStartTime = System.currentTimeMillis()
        binding.progressBar.setVisible()
        binding.textViewDuration.text = ""
        binding.textViewResult.text = ""
        disableButtons()
    }

    private fun onSuccess(uiState: UiState.Success) {
        enableButtons()
        binding.progressBar.setGone()
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
        binding.progressBar.setGone()
        binding.textViewDuration.setGone()
        toast(uiState.message)
        enableButtons()
    }

    private fun enableButtons() {
        binding.btnExceptionTryCatch.isEnabled = true
        binding.btnCoroutineExceptionHandler.isEnabled = true
        binding.btnShowResultsEvenIfChildCoroutineFailsTryCatch.isEnabled = true
        binding.btnShowResultsEvenIfChildCoroutineFailsRunCatching.isEnabled = true
    }

    private fun disableButtons() {
        binding.btnExceptionTryCatch.isEnabled = false
        binding.btnCoroutineExceptionHandler.isEnabled = false
        binding.btnShowResultsEvenIfChildCoroutineFailsTryCatch.isEnabled = false
        binding.btnShowResultsEvenIfChildCoroutineFailsRunCatching.isEnabled = false
    }
}