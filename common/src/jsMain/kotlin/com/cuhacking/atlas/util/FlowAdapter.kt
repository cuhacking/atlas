package com.cuhacking.atlas.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
actual class FlowAdapter<T : Any?> actual constructor(
    actual val scope: CoroutineScope,
    actual val flow: Flow<T>
) : Flow<T> by flow {

    @JsName("subscribe")
    fun subscribe(
        onEvent: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ) {
        flow
            .onEach { onEvent(it) }
            .catch { onError(it) }
            .onCompletion { onComplete() }
            .launchIn(scope)
    }
}

@JsExport
@Suppress("NON_EXPORTABLE_TYPE")
actual class FlowListAdapter<T : Any?> actual constructor(
    actual val scope: CoroutineScope,
    actual val flow: Flow<List<T>>
) : Flow<List<T>> by flow {

    @JsName("subscribe")
    fun subscribe(
        onEvent: (Array<T>) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit
    ) {
        flow
            .onEach { onEvent(it.toTypedArray()) }
            .catch { onError(it) }
            .onCompletion { onComplete() }
            .launchIn(scope)
    }
}
