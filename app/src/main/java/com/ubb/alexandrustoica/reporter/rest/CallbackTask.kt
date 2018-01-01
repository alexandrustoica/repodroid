package com.ubb.alexandrustoica.reporter.rest

interface CallbackTask<in T> {
    fun onTaskCompleted(result: T)
}
