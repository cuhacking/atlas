package com.cuhacking.mapbox

import com.cuhacking.mapbox.expressions.Expression

expect abstract class Layer() {
    val id: String
    var source: String?
    var filter: Expression?
    var layout: List<Expression?>
    var paint: List<Expression?>
    var minZoom: Float
    var maxZoom: Float
}
