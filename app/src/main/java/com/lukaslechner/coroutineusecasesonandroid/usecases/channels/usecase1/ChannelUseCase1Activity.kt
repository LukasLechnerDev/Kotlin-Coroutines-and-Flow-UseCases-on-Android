package com.lukaslechner.coroutineusecasesonandroid.usecases.channels.usecase1

import android.os.Bundle
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityChannelsUsecase1Binding
import com.lukaslechner.coroutineusecasesonandroid.views.BaseActivity

class ChannelUseCase1Activity : BaseActivity() {

    private val binding by lazy { ActivityChannelsUsecase1Binding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun getToolbarTitle() = "Channel Use Case 1"
}