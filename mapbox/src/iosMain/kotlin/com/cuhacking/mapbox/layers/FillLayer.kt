package com.cuhacking.mapbox.layers

import cocoapods.Mapbox.MGLFillStyleLayer
import cocoapods.Mapbox.MGLSource
import com.cuhacking.mapbox.expressions.Expression
import com.cuhacking.mapbox.expressions.ExpressionLiteral
import com.cuhacking.mapbox.expressions.toNSExpression

actual class FillLayer actual constructor(
    actual override val id: String,
    actual override val sourceId: String,
    actual override val properties: Map<String, Expression>,
    actual override val filter: Expression?,
) : Layer {
    fun getMglLayer(source: MGLSource): MGLFillStyleLayer {
        return MGLFillStyleLayer(id, source).apply {

            properties["fill-antialias"]?.let { expression ->
                fillAntialiased = expression.toNSExpression()
            }
            properties["fill-color"]?.let { expression ->
                fillColor = expression.toNSExpression()
            }
            properties["fill-opacity"]?.let { expression ->
                fillOpacity = expression.toNSExpression()
            }
            properties["fill-outline-color"]?.let { expression ->
                fillOutlineColor = expression.toNSExpression()
            }
            properties["fill-pattern"]?.let { expression ->
                fillPattern = expression.toNSExpression()
            }
            properties["fill-sort-key"]?.let { expression ->
                fillSortKey = expression.toNSExpression()
            }
            properties["fill-translate"]?.let { expression ->
                fillTranslation = expression.toNSExpression()
            }
            properties["fill-translate-anchor"]?.let { expression ->
                fillTranslationAnchor = expression.toNSExpression()
            }
            properties["visibility"]?.let { expression ->
                visible = ((expression as? ExpressionLiteral)?.literal as? Boolean) ?: true
            }
        }
    }
}
