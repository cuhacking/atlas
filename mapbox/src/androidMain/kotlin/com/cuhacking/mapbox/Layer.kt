package com.cuhacking.mapbox

import com.cuhacking.mapbox.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.Layer

actual abstract class Layer actual constructor() : Layer() {
    @get:JvmName("getId_")
    actual val id: String
        get() = TODO("Not yet implemented")
    actual var source: String? = ""
    actual var filter: Expression?
        get() = TODO("Not yet implemented")
        set(value) {}
    actual var layout: List<Expression?>
        get() = TODO("Not yet implemented")
        set(value) {}
    actual var paint: List<Expression?>
        get() = TODO("Not yet implemented")
        set(value) {}
    actual var minZoom: Float
        get() = super.getMinZoom()
        set(value) {
            super.setMinZoom(value)
        }
    actual var maxZoom: Float
        get() = super.getMaxZoom()
        set(value) {
            super.setMaxZoom(value)
        }
}