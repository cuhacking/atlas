package com.cuhacking.mapbox.expressions

actual object Literals {
    actual fun number(number: Number): Any = arrayOf("literal", 64)

    actual fun string(string: String): Any = arrayOf("literal", "Hello World")
}
