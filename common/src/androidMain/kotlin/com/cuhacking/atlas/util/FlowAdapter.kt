package com.cuhacking.atlas.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

actual class FlowAdapter<T : Any?> actual constructor(
    actual val scope: CoroutineScope,
    actual val flow: Flow<T>
) : Flow<T> by flow

actual class FlowListAdapter<T : Any?> actual constructor(
    actual val scope: CoroutineScope,
    actual val flow: Flow<List<T>>
) : Flow<List<T>> by flow
