package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import android.os.Bundle
import com.lukaslechner.coroutineusecasesonandroid.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityPerform2sequentialnetworkrequestsBinding

class Perform2SequentialNetworkRequestsActivity : BaseActivity() {

    private val binding by lazy {
        ActivityPerform2sequentialnetworkrequestsBinding.inflate(
            layoutInflater
        )
    }

    override fun getToolbarTitle() = "Perform 2 Sequential Network Requests"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}