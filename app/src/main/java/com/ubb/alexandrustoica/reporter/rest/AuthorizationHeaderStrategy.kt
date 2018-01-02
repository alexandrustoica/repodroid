package com.ubb.alexandrustoica.reporter.rest

import com.ubb.alexandrustoica.reporter.domain.Token
import org.springframework.http.HttpHeaders

class AuthorizationHeaderStrategy(
        private val token: String,
        private val nextStrategyToApply: HeaderStrategy = NoHeaderStrategy())
    : HeaderStrategy {
    constructor(token: Token): this(token.value)
    override fun complete(currentHeaders: HttpHeaders): HttpHeaders =
            currentHeaders
                    .also { it.set("Authorization", token) }
                    .also { nextStrategyToApply.complete(it) }
}