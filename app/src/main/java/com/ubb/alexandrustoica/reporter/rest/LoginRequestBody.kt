package com.ubb.alexandrustoica.reporter.rest

import java.io.Serializable

data class LoginRequestBody(
        val username: String,
        val password: String) : Serializable