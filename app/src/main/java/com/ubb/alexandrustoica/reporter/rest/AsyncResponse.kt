package com.ubb.alexandrustoica.reporter.rest

data class AsyncResponse<out R, out E>(
        private val result: R? = null,
        private val error: E? = null) {

    fun ifResult(consumer: (R) -> Unit): AsyncResponse<R, E> =
            result?.let(consumer).let { this }

    fun ifError(consumer: (E) -> Unit): AsyncResponse<R, E> =
            error?.let(consumer).let { this }
}