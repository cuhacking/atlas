package com.cuhacking.mapbox

import cocoapods.Mapbox.MGLShapeSource
import cocoapods.Mapbox.MGLShapeSourceMeta
import cocoapods.Mapbox.MGLSource
import cocoapods.Mapbox.MGLShape
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.FeatureCollection.Companion.toFeatureCollection
import io.github.dellisd.spatialk.geojson.Geometry
import kotlinx.serialization.json.Json

actual class GeoJsonSource actual constructor(actual val id: String, features: FeatureCollection?) {

    val internalSource = MGLShapeSource(id, null, null)

    actual fun setGeoJson(json: String) {
        setGeoJson(json.toFeatureCollection())
    }

    actual fun setGeoJson(feature: Feature) {
        internalSource.shape = feature.toMapbox() as MGLShape
    }

    actual fun setGeoJson(geometry: Geometry) {
        internalSource.shape = geometry.toMapbox() as MGLShape
    }

    actual fun setGeoJson(featureCollection: FeatureCollection) {
        internalSource.shape = featureCollection.toMapbox()
    }
}
