package com.lukaslechner.coroutineusecasesonandroid.base

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1.PerformSingleNetworkRequestActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10.CalculationInBackgroundActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase11.CooperativeCancellationActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase12.CalculationInSeveralCoroutinesActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13.ExceptionHandlingActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14.ContinueCoroutineWhenUserLeavesScreenActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase15.WorkManagerActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase16.PerformanceAnalysisActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase17.PerformCalculationOnMainThreadActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.Perform2SequentialNetworkRequestsActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.callbacks.SequentialNetworkRequestsCallbacksActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2.rx.SequentialNetworkRequestsRxActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3.PerformNetworkRequestsConcurrentlyActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4.VariableAmountOfNetworkRequestsActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase5.NetworkRequestWithTimeoutActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6.RetryNetworkRequestActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7.TimeoutAndRetryActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7.callbacks.TimeoutAndRetryCallbackActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7.rx.TimeoutAndRetryRxActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8.RoomAndCoroutinesActivity
import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase9.DebuggingCoroutinesActivity
import kotlinx.parcelize.Parcelize

@Parcelize
data class UseCase(
    val description: String,
    val targetActivity: Class<out AppCompatActivity>
) : Parcelable

@Parcelize
data class UseCaseCategory(val categoryName: String, val useCases: List<UseCase>) : Parcelable

const val useCase1Description = "#1 Perform single network request"
const val useCase2Description = "#2 Perform two sequential network requests"
const val useCase2UsingCallbacksDescription = "#2 using Callbacks"
const val useCase2UsingRxDescription = "#2 using RxJava"
const val useCase3Description = "#3 Perform several network requests concurrently"
const val useCase4Description = "#4 Perform variable amount of network requests"
const val useCase5Description = "#5 Network request with TimeOut"
const val useCase6Description = "#6 Retry Network request"
const val useCase7Description = "#7 Network requests with timeout and retry"
const val useCase7UsingCallbacksDescription = "#7 Using callbacks"
const val useCase7UsingRxDescription = "#7 Using RxJava"
const val useCase8Description = "#8 Room and Coroutines"
const val useCase9Description = "#9 Debugging Coroutines"
const val useCase10Description = "#10 Offload expensive calculation to background thread"
const val useCase11Description = "#11 Cooperative Cancellation"
const val useCase12Description = "#12 Offload expensive calculation to several coroutines"
const val useCase13Description = "#13 Exception Handling"
const val useCase14Description = "#14 Continue Coroutine when User leaves screen"
const val useCase15Description = "#15 Using WorkManager with Coroutines"
const val useCase16Description =
    "#16 Performance Analysis of dispatchers, number of coroutines and yielding"
const val useCase17Description =
    "#17 Perform heavy calculation on Main Thread without freezing the UI"

private val coroutinesUseCases =
    UseCaseCategory(
        "Coroutine Use Cases", listOf(
            UseCase(
                useCase1Description,
                PerformSingleNetworkRequestActivity::class.java
            ),
            UseCase(
                useCase2Description,
                Perform2SequentialNetworkRequestsActivity::class.java
            ),
            UseCase(
                useCase2UsingCallbacksDescription,
                SequentialNetworkRequestsCallbacksActivity::class.java
            ), UseCase(
                useCase2UsingRxDescription,
                SequentialNetworkRequestsRxActivity::class.java
            ),
            UseCase(
                useCase3Description,
                PerformNetworkRequestsConcurrentlyActivity::class.java
            ),
            UseCase(
                useCase4Description,
                VariableAmountOfNetworkRequestsActivity::class.java
            ),
            UseCase(
                useCase5Description,
                NetworkRequestWithTimeoutActivity::class.java
            ),
            UseCase(
                useCase6Description,
                RetryNetworkRequestActivity::class.java
            ),
            UseCase(
                useCase7Description,
                TimeoutAndRetryActivity::class.java
            ),
            UseCase(
                useCase7UsingCallbacksDescription,
                TimeoutAndRetryCallbackActivity::class.java
            ), UseCase(
                useCase7UsingRxDescription,
                TimeoutAndRetryRxActivity::class.java
            ),
            UseCase(
                useCase8Description,
                RoomAndCoroutinesActivity::class.java
            ),
            UseCase(
                useCase9Description,
                DebuggingCoroutinesActivity::class.java
            ),
            UseCase(
                useCase10Description,
                CalculationInBackgroundActivity::class.java
            ),
            UseCase(
                useCase11Description,
                CooperativeCancellationActivity::class.java
            ),
            UseCase(
                useCase12Description,
                CalculationInSeveralCoroutinesActivity::class.java
            ),
            UseCase(
                useCase13Description,
                ExceptionHandlingActivity::class.java
            ),
            UseCase(
                useCase14Description,
                ContinueCoroutineWhenUserLeavesScreenActivity::class.java
            ),
            UseCase(
                useCase15Description,
                WorkManagerActivity::class.java
            ),
            UseCase(
                useCase16Description,
                PerformanceAnalysisActivity::class.java
            ),
            UseCase(
                useCase17Description,
                PerformCalculationOnMainThreadActivity::class.java
            )
        )
    )

val useCaseCategories = listOf(
    coroutinesUseCases
)