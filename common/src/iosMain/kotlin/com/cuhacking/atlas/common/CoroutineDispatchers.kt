package com.cuhacking.atlas.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * At the moment, Kotlin/Native doesn't really support multi-threaded coroutines,
 * so we'll just stick with the Main thread for all coroutines
 */
actual object CoroutineDispatchers {
    actual val main: CoroutineDispatcher = Dispatchers.Main
    actual val io: CoroutineDispatcher = Dispatchers.Main
    actual val computation: CoroutineDispatcher = Dispatchers.Main
}
