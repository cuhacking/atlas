package com.cuhacking.atlas.db

import com.squareup.sqldelight.ColumnAdapter
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Feature.Companion.toFeature

object GeoJsonAdapter : ColumnAdapter<Feature, String> {
    override fun decode(databaseValue: String): Feature = databaseValue.toFeature()

    override fun encode(value: Feature): String = value.json
}
