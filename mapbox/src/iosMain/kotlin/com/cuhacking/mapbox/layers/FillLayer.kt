package com.cuhacking.mapbox.layers

import com.cuhacking.mapbox.expressions.Expression

actual class FillLayer actual constructor(
    actual override val id: String,
    actual override val sourceId: String,
    actual override val properties: Map<String, Expression>,
    actual override val filter: Expression?,
) : Layer {
    /* TODO */
}
