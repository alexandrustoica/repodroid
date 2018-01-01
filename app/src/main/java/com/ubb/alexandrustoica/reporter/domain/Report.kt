package com.ubb.alexandrustoica.reporter.domain

import java.io.Serializable
import java.util.*

data class Report(
        val id: Int = 0,
        val text: String = "default",
        val location: Location = Location(0.0, 0.0),
        val date: Calendar = Calendar.getInstance()) : Serializable