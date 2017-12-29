package com.ubb.alexandrustoica.reporter.task

interface CompletedTask<in T> {
    fun onTaskCompleted(result: T)
}