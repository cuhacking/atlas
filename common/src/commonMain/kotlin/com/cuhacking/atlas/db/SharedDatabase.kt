package com.cuhacking.atlas.db

import com.squareup.sqldelight.db.SqlDriver

lateinit var database: Database

expect fun provideDbDriver(): SqlDriver

fun SqlDriver.createDatabase() : Database {

    return Database(
            driver = this,
            featureAdapter = Feature.Adapter(
                    jsonAdapter = GeoJsonAdapter
            )
    )

}



