package com.cuhacking.atlas.db

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.runBlocking

lateinit var appContext: Context

actual suspend fun provideDbDriver(): SqlDriver {
    return AndroidSqliteDriver(AtlasDatabase.Schema, appContext, "Feature.db")
}
