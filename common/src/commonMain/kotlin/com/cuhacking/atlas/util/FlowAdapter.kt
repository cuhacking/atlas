package com.cuhacking.atlas.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

expect class FlowAdapter<T : Any?>(scope: CoroutineScope, flow: Flow<T>) : Flow<T> {
    val scope: CoroutineScope
    val flow: Flow<T>
}

fun <T : Any?> Flow<T>.adapt(scope: CoroutineScope): FlowAdapter<T> = FlowAdapter(scope, this)
