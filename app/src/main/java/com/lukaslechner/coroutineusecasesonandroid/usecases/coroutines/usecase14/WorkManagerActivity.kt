package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import android.os.Bundle
import androidx.activity.viewModels
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityWorkmangerBinding
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class WorkManagerActivity : BaseActivity() {

    override fun getToolbarTitle() = "Using WorkManager with Coroutines"

    private val binding by lazy { ActivityWorkmangerBinding.inflate(layoutInflater) }
    private val viewModel: WorkManagerViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.performAnalyticsRequest()
    }
}