package com.cuhacking.atlas.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * JS is single-threaded, so there is only the Main thread
 */
actual object CoroutineDispatchers {
    actual val main: CoroutineDispatcher = Dispatchers.Main
    actual val io: CoroutineDispatcher = Dispatchers.Main
    actual val computation: CoroutineDispatcher = Dispatchers.Main
}
