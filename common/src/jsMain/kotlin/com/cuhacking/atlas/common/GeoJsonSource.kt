package com.cuhacking.atlas.common

import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.github.dellisd.spatialk.geojson.Geometry


@ExperimentalJsExport
@JsExport
actual class GeoJsonSource actual constructor(actual val id: String, features: FeatureCollection?) {
    val type = "geojson"
    var data: dynamic = null

    actual fun setGeoJson(json: String) {
        data = JSON.parse(json)
    }

    @JsName("setGeoJsonFeature")
    actual fun setGeoJson(feature: Feature) {
        data = JSON.parse(feature.json)
    }

    @JsName("setGeoJsonGeometry")
    actual fun setGeoJson(geometry: Geometry) {
        data = JSON.parse(geometry.json)
    }

    @JsName("setGeoJsonFeatureCollection")
    actual fun setGeoJson(featureCollection: FeatureCollection) {
        data = JSON.parse(featureCollection.json)
    }
}
