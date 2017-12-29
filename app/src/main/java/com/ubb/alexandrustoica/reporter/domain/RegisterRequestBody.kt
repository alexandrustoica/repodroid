package com.ubb.alexandrustoica.reporter.domain

import java.io.Serializable

data class RegisterRequestBody(
        private val username: String,
        private val name: String,
        private val email: String,
        private val password: String) :
        Serializable