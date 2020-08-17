package com.cuhacking.mapbox.test

import com.cuhacking.mapbox.expressions.Expression
import com.cuhacking.mapbox.expressions.toMapbox
import kotlin.test.assertEquals
import com.mapbox.mapboxsdk.style.expressions.Expression as MapboxExpression

/**
 * Utility assertion for comparing an [Expression] with its platform-specific equivalent.
 *
 * @param expression The expression to compare. An appropriate `toMapbox()` method will be called for comparison to [platform]
 * @param platform The expected platform-specific value that [expression] should equal after being converted
 * @param message A message to display if the assertion fails
 */
actual fun assertMapboxEquivalent(
    expression: Expression,
    platform: Any,
    message: String?
) {
    assertEquals(expression.toMapbox(), platform as MapboxExpression, message)
}