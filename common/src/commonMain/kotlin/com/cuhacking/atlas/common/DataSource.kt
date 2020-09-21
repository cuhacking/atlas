package com.cuhacking.atlas.common

import com.cuhacking.mapbox.GeoJsonSource

@Suppress("MaximumLineLength", "MaxLineLength")
private const val FEATURE_JSON =
    """{"type":"FeatureCollection","features":[{"type":"Feature","properties":{},"geometry":{"type":"Polygon","coordinates":[[[-75.69827377796173,45.39140195535068],[-75.69802701473236,45.39140195535068],[-75.69802701473236,45.392049912726584],[-75.69827377796173,45.392049912726584],[-75.69827377796173,45.39140195535068]]]}},{"type":"Feature","properties":{},"geometry":{"type":"Polygon","coordinates":[[[-75.69746375083923,45.39141325699649],[-75.69719552993774,45.39141325699649],[-75.69719552993774,45.392049912726584],[-75.69746375083923,45.392049912726584],[-75.69746375083923,45.39141325699649]]]}},{"type":"Feature","properties":{},"geometry":{"type":"Polygon","coordinates":[[[-75.69878876209258,45.39117215472943],[-75.69864392280579,45.390949887665876],[-75.69835960865021,45.39088584445143],[-75.69794118404388,45.390870775449265],[-75.69744229316711,45.39085947369498],[-75.69701850414276,45.39089337895103],[-75.69679319858551,45.390953654911506],[-75.69669127464294,45.39110434453151],[-75.69677174091339,45.39119852533998],[-75.69687902927399,45.39109681006006],[-75.6971150636673,45.39102146529023],[-75.69743692874908,45.39101016356611],[-75.69775879383087,45.39099886183969],[-75.69805383682251,45.391044068731716],[-75.69833815097809,45.39113824964064],[-75.69861173629761,45.39125503374973],[-75.69878876209258,45.39126256820012],[-75.69878876209258,45.39117215472943]]]}}]}"""

val exampleDataSource = GeoJsonSource("example", null).apply {
    setGeoJson(FEATURE_JSON)
}