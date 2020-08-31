package com.cuhacking.mapbox.expressions

import com.mapbox.mapboxsdk.style.expressions.Expression

actual object Literals {
    actual fun number(number: Number): Any = Expression.literal(number)

    actual fun string(string: String): Any = Expression.literal(string)
}
