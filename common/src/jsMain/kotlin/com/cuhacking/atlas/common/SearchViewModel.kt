package com.cuhacking.atlas.common

import kotlinx.coroutines.GlobalScope

@JsExport
fun provideSearchViewModel(): SearchViewModel = SearchViewModel(CoroutineDispatchers, GlobalScope)
