package com.ubb.alexandrustoica.reporter.rest

import org.springframework.http.HttpHeaders

interface HeaderStrategy {
    fun complete(currentHeaders: HttpHeaders): HttpHeaders
}