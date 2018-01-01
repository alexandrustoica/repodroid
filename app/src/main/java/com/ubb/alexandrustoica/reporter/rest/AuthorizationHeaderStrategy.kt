package com.ubb.alexandrustoica.reporter.rest

import org.springframework.http.HttpHeaders

class AuthorizationHeaderStrategy(
        private val token: String,
        private val nextStrategyToApply: HeaderStrategy = NoHeaderStrategy())
    : HeaderStrategy {
    override fun complete(currentHeaders: HttpHeaders): HttpHeaders =
            currentHeaders
                    .also { it.set("Authorization", token) }
                    .also { nextStrategyToApply.complete(it) }
}