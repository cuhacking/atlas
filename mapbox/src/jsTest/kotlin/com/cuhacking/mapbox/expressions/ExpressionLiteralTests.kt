package com.cuhacking.mapbox.expressions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

actual class ExpressionLiteralTests {
    @Test
    actual fun testLiteralNumberMapping() {
        val value = literal(64)

        assertTrue(value.toMapbox() contentEquals arrayOf<Any>("literal", 64))
    }

    @Test
    actual fun testLiteralStringMapping() {
        val value = literal("Hello World")

        assertTrue(value.toMapbox() contentEquals arrayOf<Any>("literal", "Hello World"))
    }
}
