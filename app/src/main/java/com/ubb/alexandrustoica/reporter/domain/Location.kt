package com.ubb.alexandrustoica.reporter.domain

import java.io.Serializable

data class Location(
        val latitude: Double,
        val longitude: Double) : Serializable {
    constructor(): this(0.0, 0.0)
}