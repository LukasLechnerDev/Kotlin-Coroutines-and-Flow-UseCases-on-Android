package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase1

import android.os.Bundle
import com.lukaslechner.coroutineusecasesonandroid.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityFlowUsecase1Binding

class FlowUseCase1Activity : BaseActivity() {

    private val binding by lazy { ActivityFlowUsecase1Binding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun getToolbarTitle() = "Flow Use Case 1 Activity"

}