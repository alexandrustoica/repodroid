package com.ubb.alexandrustoica.reporter.domain

import java.io.Serializable

data class LoginRequestBody(
        private val username: String,
        private val password: String) :
        Serializable