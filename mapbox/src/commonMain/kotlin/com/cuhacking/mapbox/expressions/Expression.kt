package com.cuhacking.mapbox.expressions

open class Expression internal constructor(val operator: String?, vararg val arguments: Expression) {
    internal constructor() : this(null)
}
