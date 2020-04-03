package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityCooperativecancellationBinding
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10.CooperativeCancellationViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.hideKeyboard
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class CooperativeCancellationActivity : BaseActivity() {

    override fun getToolbarTitle() = "Cooperative Cancellation"

    private val binding by lazy { ActivityCooperativecancellationBinding.inflate(layoutInflater) }
    private val viewModel: CooperativeCancellationViewModel by viewModels()

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

    private fun onLoad() = with(binding) {
        progressBar.setVisible()
        textViewResult.text = ""
        textViewCalculationDuration.text = ""
        textViewStringConversionDuration.text = ""
        btnCalculate.isEnabled = false
        btnCancel.isEnabled = true
        textViewResult.hideKeyboard()
    }

    private fun onSuccess(uiState: UiState.Success) = with(binding) {
        textViewCalculationDuration.text =
            getString(R.string.duration_calculation, uiState.computationDuration)

        textViewStringConversionDuration.text =
            getString(R.string.duration_stringconversion, uiState.stringConversionDuration)

        binding.progressBar.setGone()
        btnCalculate.isEnabled = true
        btnCancel.isEnabled = false

        textViewResult.text = if (uiState.result.length <= 150) {
            uiState.result
        } else {
            "${uiState.result.substring(0, 147)}..."
        }
    }

    private fun onError(uiState: UiState.Error) = with(binding) {
        progressBar.setGone()
        btnCalculate.isEnabled = true
        btnCancel.isEnabled = false
        toast(uiState.message)
    }
}