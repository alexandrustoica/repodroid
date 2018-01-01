package com.ubb.alexandrustoica.reporter.rest

import java.io.Serializable

data class RegisterRequestBody(
        val username: String,
        val name: String,
        val email: String,
        val password: String) : Serializable