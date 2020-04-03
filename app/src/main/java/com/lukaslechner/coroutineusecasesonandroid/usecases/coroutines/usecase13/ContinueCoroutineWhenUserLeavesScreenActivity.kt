package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.lukaslechner.coroutineusecasesonandroid.CoroutineUsecasesOnAndroidApplication
import com.lukaslechner.coroutineusecasesonandroid.R
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityQueryfromroomdatabaseBinding
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13.ContinueCoroutineWhenUserLeavesScreenViewModel.UiState
import com.lukaslechner.coroutineusecasesonandroid.utils.fromHtml
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class ContinueCoroutineWhenUserLeavesScreenActivity : BaseActivity() {

    override fun getToolbarTitle() = "Continue Coroutine when user leaves screen"

    private val binding by lazy { ActivityQueryfromroomdatabaseBinding.inflate(layoutInflater) }

    private val viewModel: ContinueCoroutineWhenUserLeavesScreenViewModel by viewModels {
        ViewModelFactory((application as CoroutineUsecasesOnAndroidApplication).androidVersionRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.uiState().observe(this, Observer { uiState ->
            if (uiState != null) {
                render(uiState)
            }
        })
        binding.btnLoadData.setOnClickListener {
            viewModel.loadData()
        }
        binding.btnClearDatabase.setOnClickListener {
            viewModel.clearDatabase()
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                onLoad(uiState)
            }
            is UiState.Success -> {
                onSuccess(uiState)
            }
            is UiState.Error -> {
                onError(uiState)
            }
        }
    }

    private fun onLoad(loadingState: UiState.Loading) {
        when (loadingState) {
            UiState.Loading.LoadFromDb -> {
                binding.progressBarLoadFromDb.setVisible()
                binding.textViewLoadFromDatabase.setVisible()
                binding.imageViewDatabaseLoadSuccessOrError.setGone()
            }
            UiState.Loading.LoadFromNetwork -> {
                binding.progressBarLoadFromNetwork.setVisible()
                binding.textViewLoadFromNetwork.setVisible()
                binding.imageViewNetworkLoadSuccessOrError.setGone()
            }
        }
    }

    private fun onSuccess(uiState: UiState.Success) {
        when (uiState.dataSource) {
            DataSource.Network -> {
                binding.progressBarLoadFromNetwork.setGone()
                binding.imageViewNetworkLoadSuccessOrError.setImageDrawable(getDrawable(R.drawable.ic_check_green_24dp))
                binding.imageViewNetworkLoadSuccessOrError.setVisible()
            }
            DataSource.Database -> {
                binding.progressBarLoadFromDb.setGone()
                binding.imageViewDatabaseLoadSuccessOrError.setImageDrawable(getDrawable(R.drawable.ic_check_green_24dp))
                binding.imageViewDatabaseLoadSuccessOrError.setVisible()
            }
        }

        val readableVersions = uiState.recentVersions.map { "API ${it.apiVersion}: ${it.name}" }
        binding.textViewResult.text = fromHtml(
            "<b>Recent Android Versions [from ${uiState.dataSource.name}]</b><br>${readableVersions.joinToString(
                separator = "<br>"
            )}"
        )
    }

    private fun onError(uiState: UiState.Error) {
        when (uiState.dataSource) {
            is DataSource.Network -> {
                binding.progressBarLoadFromNetwork.setGone()
                binding.imageViewNetworkLoadSuccessOrError.setImageDrawable(getDrawable(R.drawable.ic_clear_red_24dp))
                binding.imageViewNetworkLoadSuccessOrError.setVisible()
            }
            is DataSource.Database -> {
                binding.progressBarLoadFromDb.setGone()
                binding.imageViewDatabaseLoadSuccessOrError.setImageDrawable(getDrawable(R.drawable.ic_clear_red_24dp))
                binding.imageViewDatabaseLoadSuccessOrError.setVisible()
            }
        }
        toast(uiState.message)
    }
}