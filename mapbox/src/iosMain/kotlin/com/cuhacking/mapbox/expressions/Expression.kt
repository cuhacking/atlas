package com.cuhacking.mapbox.expressions

import platform.Foundation.NSExpression

fun Expression.toNSExpression(): NSExpression = when (this) {
    is ExpressionLiteral -> NSExpression.expressionForConstantValue(literal)
    else -> TODO("Not yet implemented")
}
