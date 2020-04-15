package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase15

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.base.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.base.useCase11Description
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityPerformanceanalysisBinding
import com.lukaslechner.coroutineusecasesonandroid.utils.hideKeyboard
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class PerformanceAnalysisActivity : BaseActivity() {

    override fun getToolbarTitle() = useCase11Description

    private val binding by lazy {
        ActivityPerformanceanalysisBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: PerformanceAnalysisViewModel by viewModels()
    private lateinit var selectedDispatcher: CoroutineDispatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val numberOfCores = Runtime.getRuntime().availableProcessors()
        binding.textViewNumberOfCores.text = getString(R.string.device_cores, numberOfCores)
        viewModel.uiState()
            .observe(this@PerformanceAnalysisActivity, Observer { uiState ->
                if (uiState != null) {
                    render(uiState)
                }
            })
        binding.btnCalculate.setOnClickListener {
            val factorialOf = binding.editTextFactorialOf.text.toString().toIntOrNull()
            val numberOfThreads = binding.editTextNumberOfThreads.text.toString().toIntOrNull()
            if (factorialOf != null && numberOfThreads != null) {
                viewModel.performCalculation(
                    factorialOf,
                    numberOfThreads,
                    selectedDispatcher,
                    binding.switchYield.isChecked
                )
            }
        }
        ArrayAdapter.createFromResource(
            this@PerformanceAnalysisActivity,
            R.array.dispatchers,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerDispatcher.adapter = adapter
        }

        binding.spinnerDispatcher.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (parent?.getItemAtPosition(position)) {
                        "Default" -> selectedDispatcher = Dispatchers.Default
                        "IO" -> selectedDispatcher = Dispatchers.IO
                        "Main" -> selectedDispatcher = Dispatchers.Main
                        "Unconfined" -> selectedDispatcher = Dispatchers.Unconfined
                    }
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

    private fun onLoad() = with(binding) {
        progressBar.setVisible()
        textViewResult.text = ""
        textViewDuration.text = ""
        textViewStringConversionDuration.text = ""
        btnCalculate.isEnabled = false
        textViewResult.hideKeyboard()
    }

    private fun onSuccess(uiState: UiState.Success) = with(binding) {
        textViewDuration.text =
            getString(R.string.duration_calculation, uiState.computationDuration)
        textViewStringConversionDuration.text =
            getString(R.string.duration_stringconversion, uiState.stringConversionDuration)
        progressBar.setGone()
        btnCalculate.isEnabled = true
        textViewResult.text = if (uiState.result.length <= 150) {
            uiState.result
        } else {
            "${uiState.result.substring(0, 147)}..."
        }
    }

    private fun onError(uiState: UiState.Error) = with(binding) {
        progressBar.setGone()
        btnCalculate.isEnabled = true
        toast(uiState.message)
    }
}