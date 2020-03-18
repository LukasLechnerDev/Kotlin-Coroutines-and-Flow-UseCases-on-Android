package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import android.os.Bundle
import com.lukaslechner.coroutineusecasesonandroid.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityPerformsinglenetworkrequestBinding

class PerformSingleNetworkRequestActivity : BaseActivity() {

    override fun getToolbarTitle() = "Perform Single Network Request"

    private val binding by lazy { ActivityPerformsinglenetworkrequestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}