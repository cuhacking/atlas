package com.cuhacking.mapbox.expressions

import com.cuhacking.mapbox.test.assertMapboxEquivalent
import kotlin.test.Test

expect object Literals {
    fun number(number: Number): Any

    fun string(string: String): Any
}

class ExpressionLiteralTests {
    @Test
    fun testLiteralNumberMapping() {
        assertMapboxEquivalent(literal(64), Literals.number(64))
    }

    @Test
    fun testLiteralStringMapping() {
        assertMapboxEquivalent(literal("Hello World"), Literals.string("Hello World"))
    }
}
