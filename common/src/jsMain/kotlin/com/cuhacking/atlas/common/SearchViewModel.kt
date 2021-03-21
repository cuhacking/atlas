package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.SharedDatabase
import com.cuhacking.atlas.db.provideDbDriver
import kotlinx.coroutines.GlobalScope

@JsExport
fun provideSearchViewModel(): SearchViewModel =
    SearchViewModel(CoroutineDispatchers, GlobalScope, SharedDatabase(::provideDbDriver))
