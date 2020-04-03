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

    override fun getToolbarTitle() = "Calculation in the background"

    private val binding by lazy { ActivityCalculationinbackgroundBinding.inflate(layoutInflater) }
    private val viewModel: CalculationInBackgroundViewModel by viewModels()

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
                viewModel.performCalculation(factorialOf)
            }
        }
        binding.btnCancel.setOnClickListener {
            viewModel.cancelCalculation()
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
        binding.textViewCalculationDuration.text = ""
        binding.textViewStringConversionDuration.text = ""
        binding.btnCalculate.isEnabled = false
        binding.btnCancel.isEnabled = true
        binding.textViewResult.hideKeyboard()
    }

    private fun onSuccess(uiState: UiState.Success) {
        binding.textViewCalculationDuration.text =
            getString(R.string.duration_calculation, uiState.computationDuration)

        binding.textViewStringConversionDuration.text =
            getString(R.string.duration_stringconversion, uiState.stringConversionDuration)

        binding.progressBar.setGone()
        binding.btnCalculate.isEnabled = true
        binding.btnCancel.isEnabled = false

        binding.textViewResult.text = if (uiState.result.length <= 150) {
            uiState.result
        } else {
            "${uiState.result.substring(0, 147)}..."
        }
    }

    private fun onError(uiState: UiState.Error) {
        binding.progressBar.setGone()
        binding.btnCalculate.isEnabled = true
        binding.btnCancel.isEnabled = false
        toast(uiState.message)
    }
}