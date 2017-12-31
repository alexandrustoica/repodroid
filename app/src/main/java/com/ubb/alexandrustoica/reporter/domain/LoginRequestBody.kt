package com.ubb.alexandrustoica.reporter.domain

import java.io.Serializable

data class LoginRequestBody(
        val username: String,
        val password: String) : Serializable