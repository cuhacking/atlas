package com.cuhacking.atlas.common

import com.cuhacking.atlas.db.sharedDatabase
import kotlinx.coroutines.MainScope

fun provideSearchViewModel(): SearchViewModel =
    SearchViewModel(CoroutineDispatchers, MainScope(), sharedDatabase)
