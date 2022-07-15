package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.lukaslechner.coroutineusecasesonandroid.base.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityFlowUsecase1Binding
import java.time.Instant

class FlowUseCase1Activity : BaseActivity() {

    private val binding by lazy { ActivityFlowUsecase1Binding.inflate(layoutInflater) }

    private val viewModel: FlowUseCase1ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.whileTrueInCoroutine()
        viewModel.bitcoinPrice.observe(this) { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Success -> {
                binding.progressBar.visibility = View.GONE
                binding.lastFetch.text = Instant.now().epochSecond.toString()
                binding.bitcoinPrice.text = uiState.bitcoinPrice.usd.toString()
            }
        }
    }

    override fun getToolbarTitle() = "Flow Use Case 1 Activity"
}