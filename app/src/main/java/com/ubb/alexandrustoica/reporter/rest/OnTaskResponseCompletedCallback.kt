package com.ubb.alexandrustoica.reporter.rest

interface OnTaskResponseCompletedCallback<in T> {
    fun onTaskCompleted(result: T)
}
