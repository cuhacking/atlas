package com.cuhacking.atlas.common

import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Geometry

actual class GeoJsonSource actual constructor(actual val id: String, features: FeatureCollection?) {
    init {
        TODO("Not implemented")
    }

    actual fun setGeoJson(json: String) {
        TODO("Not implemented")
    }

    actual fun setGeoJson(feature: Feature) {
        TODO("Not implemented")
    }

    actual fun setGeoJson(geometry: Geometry) {
        TODO("Not implemented")
    }

    actual fun setGeoJson(featureCollection: FeatureCollection) {
        TODO("Not implemented")
    }
}
