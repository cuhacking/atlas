package com.cuhacking.atlas.common

import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Geometry

actual class GeoJsonSource actual constructor(
    @get:JvmName("getId_") actual val id: String,
    features: FeatureCollection?
) : GeoJsonSource(id) {
    actual fun setGeoJson(feature: Feature) {
        setGeoJson(feature.toMapbox())
    }

    actual fun setGeoJson(geometry: Geometry) {
        setGeoJson(geometry.toMapbox())
    }

    actual fun setGeoJson(featureCollection: FeatureCollection) {
        setGeoJson(featureCollection.toMapbox())
    }
}