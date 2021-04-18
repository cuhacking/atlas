package com.cuhacking.mapbox.expressions

import platform.Foundation.NSExpression
import platform.UIKit.UIColor

fun Expression.toNSExpression(): NSExpression = when (this) {
    is ColorExpression -> NSExpression.expressionForConstantValue(hexToUIColor(hexColor))
    is ExpressionLiteral -> NSExpression.expressionForConstantValue(literal)
    else -> TODO("Not yet implemented")
}

@Suppress("MagicNumber")
private fun hexToUIColor(hex: String): UIColor {
    val asInt = hex.removePrefix("#").toUInt(radix = 16)

    val red = (asInt shr 16) and 0xFFU
    val green = (asInt shr 8) and 0xFFU
    val blue = asInt and 0xFFU

    return UIColor(
        red = (red.toDouble() / 255.0),
        green = green.toDouble() / 255.0,
        blue = blue.toDouble() / 255.0,
        alpha = 1.0
    )
}
