![CoroutineUsecasesOnAndroid](CoroutinesUsecasesGithubLogo.png)

# Coroutine Use Cases on Android

Learning Coroutines for Android by Example. Sample Implementations for Common Android Use Cases. Unit Tests included!

This repository is intended to be a "Playground Project". You can quickly look up and play around with the different Coroutine Android implementations.
In the `playground` package you can play around with Coroutines examples that run directly on the JVM.

## Project Setup

Every use case is using its own `Activity` and `JetPack ViewModel`. The `ViewModel`s contains all the interesting Coroutine related code.
`Activities` listen to `LiveData` events of the `ViewModel` and render received `UiState`s.

This project is using retrofit/okhttp together with a `MockNetworkInterceptor`. This lets you define how the API should behave.
Everything can be configured: http status codes, response data and delays. Every use case defines a certain behaviour of the Mock API.
The API has 2 endpoints. One returns the names of the most recent Android versions and the other one returns the features of a certain
Android version.

Unit Tests exist for most use cases.

## Use Cases
1. [Perform single network request](#1-perform-a-single-network-request)
2. [Perform two sequential network requests](#2-perform-two-sequential-network-requests)
3. [Perform several network requests concurrently](#3-perform-several-network-requests-concurrently)
4. Perform a variable amount of network requests, depending on the amount of data sets received from the first network request
5. Perform a network request with a timeout
6. Retrying network requests
7. [Room and Coroutines](#7-room-and-coroutines)
8. [Debugging Coroutines](#8-debugging-coroutines)
8. Offload a heavy calculation from the main thread
9. Offload a heavy calculation to several Coroutines
10. Handling Exceptions
11. Coroutine Cancellation
12. Continue Coroutine execution even when the user has already left the screen
14. [Using WorkManager with Coroutines](#14-using-workmanager-with-coroutines)

## Description

### 1. Perform single network request

Performs a single network request to get the latest Android Versions.

### 2. Perform two sequential network requests

Performs two network requests sequentially. First it retrieves recent Android Versions and then it requests the features of the latest version.
There also exists an alternative implementation for this use case which uses traditional callbacks. It should demonstrate how much shorter and more readable the Coroutine version is compared to the approach with callbacks.

### 3. Perform several network requests concurrently

Performs three network requests concurrently. It loads the feature information of the 3 most recent Android Versions. Additionally, an implementation
that performs the requests sequentially is included. The UI shows how much time each implementation takes to load the data so you can see that the network
requests in the concurrent version are actually performed in parallel.

### 7. Room and Coroutines

This example stores the response data of each network request in a Room database. This is essential for any "offline-first" app.
If the `View` requests data, the `ViewModel` first checks if there is data available in the database. If so, this data is returned before performing
a network request to get fresh data.

### 8. Debugging Coroutines

Shows how you can add additional debug information about the Coroutine that is currently running to your logs.
It will add the Coroutine name next to the thread name when calling `Thread.currentThread.name()`
This is done by enabling Coroutine Debug mode by setting the property `kotlinx.coroutines.debug` to `true`.
This is how it will look like in LogCat:

![DebuggingCoroutines](documentation/images/debugging_coroutines.png)

### 14. Using WorkManager with Coroutines

Demonstrates how you can use WorkManager together with Coroutines. When creating a subclass of `CoroutineWorker` instead of `Worker`,
the `doWork()` function is now a `suspend function` which means that we can now call other suspend functions. In this
example, we are sending an analytics request when the user enters the screen, which is a nice use case for using WorkManager.

## Contributing

I am currently learning Coroutines myself. So if you have any ideas for or improvements or other use cases, feel free to create a pull request or an issue.

## License

Licensed under the Apache License, Version 2.0 (the "License").
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

You agree that all contributions to this repository, in the form of fixes, pull-requests, new examples etc. follow the above-mentioned license.
