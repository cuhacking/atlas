package com.cuhacking.mapbox.expressions

import com.mapbox.mapboxsdk.style.expressions.Expression as MapboxExpression
import com.mapbox.mapboxsdk.style.expressions.Expression.ExpressionLiteral as MapboxExpressionLiteral

fun Expression.toMapbox(): MapboxExpression = when (this) {
    is ExpressionLiteral -> MapboxExpressionLiteral(literal)
    else -> MapboxExpression(operator!!, *arguments.map(Expression::toMapbox).toTypedArray())
}
