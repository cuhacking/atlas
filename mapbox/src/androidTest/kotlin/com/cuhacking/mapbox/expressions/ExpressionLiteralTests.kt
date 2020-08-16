package com.cuhacking.mapbox.expressions

import com.mapbox.mapboxsdk.style.expressions.Expression
import kotlin.test.Test
import kotlin.test.assertEquals

actual class ExpressionLiteralTests {
    @Test
    actual fun testLiteralNumberMapping() {
        val value = literal(64)
        val mapbox = Expression.literal(64)

        assertEquals(value.toMapbox(), mapbox)
    }

    @Test
    actual fun testLiteralStringMapping() {
        val value = literal("Hello World")
        val mapbox = Expression.literal("Hello World")

        assertEquals(value.toMapbox(), mapbox)
    }
}