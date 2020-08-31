package com.cuhacking.atlas.common

import kotlinx.coroutines.CoroutineDispatcher

expect object CoroutineDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val computation: CoroutineDispatcher
}
