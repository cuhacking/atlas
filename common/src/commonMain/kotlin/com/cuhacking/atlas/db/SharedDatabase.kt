package com.cuhacking.atlas.db

import com.squareup.sqldelight.db.SqlDriver

lateinit var database: Database

expect fun provideDbDriver(): SqlDriver

fun SqlDriver.createDatabase(): Database {
    // Uncomment the following code and delete the rest. It was used for testing only
//    return Database(
//            driver = this,
//            featureAdapter = Feature.Adapter(
//                    jsonAdapter = GeoJsonAdapter
//            )
//    )
//    Database.Schema.create(this)
    database = Database(
        driver = this,
            featureAdapter = Feature.Adapter(
                    jsonAdapter = GeoJsonAdapter
            )
    )
    database.featureQueries.deleteAll()
    database.featureQueries.clearFts()

    val sampleFeature = GeoJsonAdapter.decode("""{"type":"Feature","geometry":{"type":"Point",
        |"coordinates":[-75.0,45.0]},"properties":{}}""".trimMargin())
    val feature1 = Feature(1, "River Building", "Richcraft Hall", "Building", null, null, null, sampleFeature)
    val feature2 = Feature(2, "RB 2311", null, "room", "River Building", "RB2", null, sampleFeature)
    val feature3 = Feature(3, "HS 1301B", null, "room", "Health Sciences Building", "HS1", null, sampleFeature)

    database.featureQueries.insertFeature(feature1)
    database.featureQueries.insertFeature(feature2)
    database.featureQueries.insertFeature(feature3)

    return database
}
