package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase15

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Context::class.java)
            .newInstance(context.applicationContext)
    }
}