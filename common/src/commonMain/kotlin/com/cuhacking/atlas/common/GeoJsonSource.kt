package com.cuhacking.atlas.common

import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Geometry

expect class GeoJsonSource(id: String, features: FeatureCollection?) {

    val id: String

    fun setGeoJson(json: String)

    fun setGeoJson(feature: Feature)

    fun setGeoJson(geometry: Geometry)

    fun setGeoJson(featureCollection: FeatureCollection)
}