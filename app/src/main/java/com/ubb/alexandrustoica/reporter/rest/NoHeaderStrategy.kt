package com.ubb.alexandrustoica.reporter.rest

import org.springframework.http.HttpHeaders

class NoHeaderStrategy : HeaderStrategy {
    override fun complete(currentHeaders: HttpHeaders): HttpHeaders =
            currentHeaders
}