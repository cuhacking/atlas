package com.cuhacking.mapbox.layers

import com.cuhacking.mapbox.expressions.Expression
import com.cuhacking.mapbox.expressions.literal

expect class FillLayer(
    id: String,
    sourceId: String,
    properties: Map<String, Expression>,
    filter: Expression?
) : Layer {
    override val id: String
    override val sourceId: String
    override val properties: Map<String, Expression>
    override val filter: Expression?
}

fun fillLayer(
    id: String,
    sourceId: String,
    filter: Expression? = null,
    properties: FillLayerDsl.() -> Unit = {}
): FillLayer =
    FillLayerDsl(id, sourceId, filter).apply(properties).create()

class FillLayerDsl internal constructor(
    val id: String,
    val sourceId: String,
    val filter: Expression?
) {
    private val properties: HashMap<String, Expression> = hashMapOf()

    fun fillAntialias(boolean: Boolean) {
        fillAntialias(literal(boolean))
    }

    fun fillAntialias(expression: Expression) {
        properties["fill-antialias"] = expression
    }

    fun fillColor(hexColor: String) {
        fillColor(literal(hexColor))
    }

    fun fillColor(expression: Expression) {
        properties["fill-color"] = expression
    }

    fun fillOpacity(opacity: Double) {
        fillOpacity(literal(opacity))
    }

    fun fillOpacity(expression: Expression) {
        properties["fill-opacity"] = expression
    }

    fun fillOutlineColor(hexColor: String) {
        fillOutlineColor(literal(hexColor))
    }

    fun fillOutlineColor(expression: Expression) {
        properties["fill-outline-color"] = expression
    }

    fun fillPattern(pattern: String) {
        fillPattern(literal(pattern))
    }

    fun fillPattern(expression: Expression) {
        properties["fill-pattern"] = expression
    }

    fun fillSortKey(key: Double) {
        fillSortKey(literal(key))
    }

    fun fillSortKey(expression: Expression) {
        properties["fill-sort-key"] = expression
    }

    fun fillTranslate(translate: List<Double>) {
        fillTranslate(literal(translate.toDoubleArray()))
    }

    fun fillTranslate(expression: Expression) {
        properties["fill-translate"] = expression
    }

    fun fillTranslateAnchor(anchor: FillTranslateAnchor) {
        fillTranslateAnchor(literal(anchor.value))
    }

    fun fillTranslateAnchor(expression: Expression) {
        properties["fill-translate-anchor"] = expression
    }

    fun visibility(visibility: Visibility) {
        visibility(literal(visibility.value))
    }

    fun visibility(expression: Expression) {
        properties["visibility"] = expression
    }

    fun create(): FillLayer = FillLayer(id, sourceId, properties, filter)
}

enum class FillTranslateAnchor(val value: String) {
    MAP("map"),
    VIEWPORT("viewport")
}
