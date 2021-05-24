package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.sharedDatabase
import kotlinx.coroutines.GlobalScope

@JsExport
fun provideSearchViewModel(): SearchViewModel =
    SearchViewModel(CoroutineDispatchers, GlobalScope, sharedDatabase)
