package com.ubb.alexandrustoica.reporter.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class JsonContentHeaderStrategy(
        private val nextStrategyToApply: HeaderStrategy) : HeaderStrategy {
    override fun complete(currentHeaders: HttpHeaders): HttpHeaders =
            currentHeaders
                    .also { it.contentType = MediaType.APPLICATION_JSON }
                    .also { nextStrategyToApply.complete(it) }
}