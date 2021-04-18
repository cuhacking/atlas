package com.cuhacking.mapbox.layers

import com.cuhacking.mapbox.expressions.Expression
import kotlin.js.JsExport

@JsExport
interface Layer {
    val id: String
    val sourceId: String
    val properties: Map<String, Expression>
    val filter: Expression?
}

enum class Visibility(val value: String) {
    VISIBLE("visible"),
    NONE("none")
}
