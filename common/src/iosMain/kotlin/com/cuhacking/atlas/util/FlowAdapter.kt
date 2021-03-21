package com.cuhacking.atlas.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlin.native.concurrent.freeze

actual class FlowAdapter<T : Any?> actual constructor(
    actual val scope: CoroutineScope,
    actual val flow: Flow<T>
) : Flow<T> by flow {
    init {
        freeze()
    }

    fun subscribe(
        onEvent: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ): Job =
        flow
            .onEach { onEvent(it.freeze()) }
            .catch { onError(it.freeze()) }
            .onCompletion { onComplete() }
            .launchIn(scope)
}

actual class FlowListAdapter<T : Any?> actual constructor(
    actual val scope: CoroutineScope,
    actual val flow: Flow<List<T>>
) : Flow<List<T>> by flow {
    init {
        freeze()
    }

    fun subscribe(
        onEvent: (List<T>) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ): Job =
        flow
            .onEach { onEvent(it.freeze()) }
            .catch { onError(it.freeze()) }
            .onCompletion { onComplete() }
            .launchIn(scope)
}
