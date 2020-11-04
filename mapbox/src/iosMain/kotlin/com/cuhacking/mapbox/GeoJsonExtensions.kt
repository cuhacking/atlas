@file:Suppress("TooManyFunctions")

package com.cuhacking.mapbox

import cocoapods.Mapbox.*
import io.github.dellisd.spatialk.geojson.*
import kotlinx.cinterop.*
import kotlinx.serialization.json.*
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake

@Suppress("ForbiddenComment")
// TODO: Altitude not supported?
fun Position.toCoreLocation() = CLLocationCoordinate2DMake(latitude, longitude)

internal fun CLLocationCoordinate2D.fromPosition(position: Position) {
    latitude = position.latitude
    longitude = position.longitude
}


internal fun NativePlacement.positionsToCoreLocation(positions: List<Position>) =
    allocArray<CLLocationCoordinate2D>(positions.size) { index ->
        fromPosition(positions[index])
    }

fun Point.toMapbox(): MGLPointFeature =
    MGLPointFeature().also { it.setCoordinate(coordinates.toCoreLocation()) }

fun MultiPoint.toMapbox(): MGLPointCollectionFeature = memScoped {
    MGLPointCollectionFeature.pointCollectionWithCoordinates(
        positionsToCoreLocation(coordinates),
        coordinates.size.convert()
    )!!
}

fun LineString.toMapbox(): MGLPolylineFeature = memScoped {
    MGLPolylineFeature.polylineWithCoordinates(
        positionsToCoreLocation(coordinates),
        coordinates.size.convert()
    )
}


fun MultiLineString.toMapbox(): MGLMultiPolylineFeature = memScoped {
    MGLMultiPolylineFeature.multiPolylineWithPolylines(coordinates.map { line ->
        MGLPolyline.polylineWithCoordinates(positionsToCoreLocation(line), line.size.convert())
    })
}

internal fun List<List<Position>>.toMGLPolygon(): MGLPolygonFeature = memScoped {
    fun ringToPolygon(ring: List<Position>): MGLPolygon =
        MGLPolygon.polygonWithCoordinates(positionsToCoreLocation(ring), ring.size.convert())

    return if (size > 1) {
        MGLPolygonFeature.polygonWithCoordinates(
            positionsToCoreLocation(this@toMGLPolygon[0]),
            this@toMGLPolygon[0].size.convert(),
            this@toMGLPolygon.subList(1, size).map(::ringToPolygon)
        )
    } else {
        MGLPolygonFeature.polygonWithCoordinates(
            positionsToCoreLocation(this@toMGLPolygon[0]),
            this@toMGLPolygon[0].size.convert()
        )
    }
}

fun Polygon.toMapbox(): MGLPolygonFeature = coordinates.toMGLPolygon()

fun MultiPolygon.toMapbox(): MGLMultiPolygonFeature =
    MGLMultiPolygonFeature.multiPolygonWithPolygons(coordinates.map { polygon -> polygon.toMGLPolygon() })

fun Geometry.toMapbox(): MGLFeatureProtocol = when (this) {
    is Point -> this.toMapbox()
    is MultiPoint -> this.toMapbox()
    is LineString -> this.toMapbox()
    is MultiLineString -> this.toMapbox()
    is Polygon -> this.toMapbox()
    is MultiPolygon -> this.toMapbox()
    is GeometryCollection -> this.toMapbox()
}

fun GeometryCollection.toMapbox(): MGLShapeCollectionFeature =
    MGLShapeCollectionFeature.shapeCollectionWithShapes(geometries.map(Geometry::toMapbox))

@Suppress("UNCHECKED_CAST", "ForbiddenComment")
fun Feature.toMapbox(): MGLFeatureProtocol? {
    // TODO: support for complex object / array properties???
    val dict = properties.entries.map { (key, value) ->
        return@map when {
            value.jsonPrimitive.booleanOrNull != null -> key to value.jsonPrimitive.boolean
            value.jsonPrimitive.doubleOrNull != null -> key to value.jsonPrimitive.double
            value.jsonPrimitive.longOrNull != null -> key to value.jsonPrimitive.long
            value.jsonPrimitive.contentOrNull != null -> key to value.jsonPrimitive.content
            else -> key to null
        }
    }.toMap()

    // TODO: support for null geometries???
    val protocol = geometry?.toMapbox()!!

    protocol.identifier = id
    protocol.attributes = dict as Map<Any?, *>

    return protocol
}

fun FeatureCollection.toMapbox(): MGLShapeCollectionFeature =
    MGLShapeCollectionFeature.shapeCollectionWithShapes(features.map(Feature::toMapbox))
