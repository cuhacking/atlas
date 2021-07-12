package com.cuhacking.atlas.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_t
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.freeze

/**
 * At the moment, Kotlin/Native doesn't really support multi-threaded coroutines,
 * so we'll just stick with the Main thread for all coroutines
 */
actual object CoroutineDispatchers {
    actual val main: CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())
    actual val io: CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())
    actual val computation: CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())
}

internal class NsQueueDispatcher(private val dispatchQueue: dispatch_queue_t) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatchQueue.freeze()) {
            block.run()
        }
    }
}

 class MainScope : CoroutineScope {
    private val dispatcher = CoroutineDispatchers.main
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = dispatcher + job
}
