package com.ubb.alexandrustoica.reporter.task

interface CallbackTask<in T> {
    fun onTaskCompleted(result: T)
}
