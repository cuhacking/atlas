package com.cuhacking.atlas.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

@Suppress("NON_EXPORTABLE_TYPE")
@JsExport
actual class FlowAdapter<T : Any?> actual constructor(
    actual val scope: CoroutineScope,
    actual val flow: Flow<T>
) : Flow<T> by flow {
    fun subscribe(
        onEvent: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ): Job =
        flow
            .onEach { onEvent(it) }
            .catch { onError(it) }
            .onCompletion { onComplete() }
            .launchIn(scope)
}
