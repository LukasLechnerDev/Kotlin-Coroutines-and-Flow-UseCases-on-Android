package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityCalculationinmultiplebackgroundthreadsBinding
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10.CalculationInMultipleBackgroundThreadsViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.hideKeyboard
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class CalculationInMultipleBackgroundThreadsActivity : BaseActivity() {

    override fun getToolbarTitle() = "Calculation in multiple background threads"

    private val binding by lazy {
        ActivityCalculationinmultiplebackgroundthreadsBinding.inflate(
            layoutInflater
        )
    }
    private val viewModel: CalculationInMultipleBackgroundThreadsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val numberOfCores = Runtime.getRuntime().availableProcessors()
        binding.textViewNumberOfCores.text = getString(R.string.device_cores, numberOfCores)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnCalculate.setOnClickListener {
            val factorialOf = binding.editTextFactorialOf.text.toString().toIntOrNull()
            val numberOfThreads = binding.editTextNumberOfThreads.text.toString().toIntOrNull()
            if (factorialOf != null && numberOfThreads != null) {
                viewModel.performCalculation(factorialOf, numberOfThreads)
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
        binding.textViewDuration.text =
            getString(R.string.duration_calculation, uiState.computationDuration)
        binding.progressBar.setGone()
        binding.btnCalculate.isEnabled = true
        binding.textViewResult.text = uiState.result
    }

    private fun onError(uiState: UiState.Error) {
        binding.progressBar.setGone()
        binding.btnCalculate.isEnabled = true
        toast(uiState.message)
    }
}