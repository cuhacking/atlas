package com.cuhacking.atlas.db

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

lateinit var appContext: Context

actual fun provideDbDriver(): SqlDriver {
    return AndroidSqliteDriver(Database.Schema, appContext, "Feature.db")
}
