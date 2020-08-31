package com.cuhacking.mapbox.expressions

import platform.Foundation.NSExpression


actual object Literals {
    actual fun number(number: Number): Any = NSExpression.expressionForConstantValue(number)

    actual fun string(string: String): Any = NSExpression.expressionForConstantValue(string)
}