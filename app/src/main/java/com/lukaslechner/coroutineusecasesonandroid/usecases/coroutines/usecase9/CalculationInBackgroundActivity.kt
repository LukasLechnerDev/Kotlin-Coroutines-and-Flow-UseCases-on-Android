package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase9

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityCalculationinbackgroundBinding
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase9.CalculationInBackgroundViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.hideKeyboard
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class CalculationInBackgroundActivity : BaseActivity() {

    override fun getToolbarTitle() = "Perform Single Network Request"

    private val binding by lazy { ActivityCalculationinbackgroundBinding.inflate(layoutInflater) }
    private val viewModel: CalculationInBackgroundViewModel by viewModels()

    private var calculationStartTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnCalculate.setOnClickListener {
            val factorialOf = binding.editTextFactorialOf.text.toString().toIntOrNull()
            if (factorialOf != null) {
                calculationStartTime = System.currentTimeMillis()
                viewModel.performCalculation(factorialOf)
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
        binding.progressBar.setVisible()
        binding.textViewResult.text = ""
        binding.textViewDuration.text = ""
        binding.btnCalculate.isEnabled = false
        binding.textViewResult.hideKeyboard()
    }

    private fun onSuccess(uiState: UiState.Success) {
        binding.progressBar.setGone()
        binding.btnCalculate.isEnabled = true
        binding.textViewResult.text = uiState.result.toString()
        val duration = System.currentTimeMillis() - calculationStartTime
        binding.textViewDuration.text = getString(R.string.duration, duration)
    }

    private fun onError(uiState: UiState.Error) {
        binding.progressBar.setGone()
        binding.btnCalculate.isEnabled = true
        toast(uiState.message)
    }
}