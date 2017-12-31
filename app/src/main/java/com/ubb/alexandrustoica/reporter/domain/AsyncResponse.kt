package com.ubb.alexandrustoica.reporter.domain

data class AsyncResponse<out R, out E>(
        private val result: R? = null,
        private val error: E? = null) {

    fun ifResult(consumer: (R) -> Unit) =
            result?.let(consumer)

    fun ifError(consumer: (E) -> Unit) =
            error?.let(consumer)
}