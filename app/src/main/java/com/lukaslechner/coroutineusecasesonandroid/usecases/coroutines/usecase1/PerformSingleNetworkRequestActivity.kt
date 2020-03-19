package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import android.os.Bundle
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityPerformsinglenetworkrequestBinding
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class PerformSingleNetworkRequestActivity : BaseActivity() {

    override fun getToolbarTitle() = "Perform Single Network Request"

    private val binding by lazy { ActivityPerformsinglenetworkrequestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}