package com.cuhacking.mapbox.expressions

class ExpressionLiteral internal constructor(public val literal: Any) : Expression()

public fun literal(number: Number): Expression = ExpressionLiteral(number)

public fun literal(string: String): Expression = ExpressionLiteral(string)

public fun literal(boolean: Boolean): Expression = ExpressionLiteral(boolean)

public fun literal(any: Any): Expression = ExpressionLiteral(any)

public fun literal(array: Array<out Any>): Expression = ExpressionLiteral(array)

class ColorExpression internal constructor(public val hexColor: String) : Expression()

public fun colorLiteral(hexColor: String): Expression = ColorExpression(hexColor)
