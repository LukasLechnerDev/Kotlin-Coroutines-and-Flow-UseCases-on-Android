package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.base.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.base.useCase11Description
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityCalculationinmultiplebackgroundthreadsBinding
import com.lukaslechner.coroutineusecasesonandroid.utils.hideKeyboard
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CalculationInSeveralCoroutinesActivity : BaseActivity() {

    override fun getToolbarTitle() = useCase11Description

    private val binding by lazy {
        ActivityCalculationinmultiplebackgroundthreadsBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: CalculationInSeveralCoroutinesViewModel by viewModels()
    private lateinit var selectedDispatcher: CoroutineDispatcher

    override fun onCreate(savedInstanceState: Bundle?) = with(binding) {
        super.onCreate(savedInstanceState)
        setContentView(root)
        val numberOfCores = Runtime.getRuntime().availableProcessors()
        textViewNumberOfCores.text = getString(R.string.device_cores, numberOfCores)
        viewModel.uiState()
            .observe(this@CalculationInSeveralCoroutinesActivity, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        btnCalculate.setOnClickListener {
            val factorialOf = editTextFactorialOf.text.toString().toIntOrNull()
            val numberOfThreads = editTextNumberOfThreads.text.toString().toIntOrNull()
            if (factorialOf != null && numberOfThreads != null) {
                viewModel.performCalculation(
                    factorialOf,
                    numberOfThreads,
                    selectedDispatcher,
                    switchYield.isChecked
                )
            }
        }
        ArrayAdapter.createFromResource(
            this@CalculationInSeveralCoroutinesActivity,
            R.array.dispatchers,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDispatcher.adapter = adapter
        }

        spinnerDispatcher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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