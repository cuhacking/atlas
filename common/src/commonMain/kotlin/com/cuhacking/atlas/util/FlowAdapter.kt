package com.cuhacking.atlas.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * An adapter for [Flow<T>] on multiplatform.
 *
 * * On Android, it behaves like a regular Flow.
 * * On JS and iOS, a subscribe function is available to listen to new emissions.
 *
 * @see adapt
 */
expect class FlowAdapter<T : Any?>(scope: CoroutineScope, flow: Flow<T>) : Flow<T> {
    val scope: CoroutineScope
    val flow: Flow<T>
}

/**
 * An adapter for [Flow<List<T>>] on multiplatform, specifically for `List<T>` items.
 *
 * * On Android, it behaves like a regular Flow.
 * * On iOS, a subscribe function is available to listen to new emissions.
 * * On JS, a subscribe function is available that converts all emitted lists to `Array<T>`.
 *
 * @see listAdapt
 */
expect class FlowListAdapter<T : Any?>(scope: CoroutineScope, flow: Flow<List<T>>) : Flow<List<T>> {
    val scope: CoroutineScope
    val flow: Flow<List<T>>
}

fun <T : Any?> Flow<T>.adapt(scope: CoroutineScope): FlowAdapter<T> = FlowAdapter(scope, this)

fun <T : Any?> Flow<List<T>>.listAdapt(scope: CoroutineScope): FlowListAdapter<T> =
    FlowListAdapter(scope, this)
