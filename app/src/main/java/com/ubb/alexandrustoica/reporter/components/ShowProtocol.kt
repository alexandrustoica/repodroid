package com.ubb.alexandrustoica.reporter.components

interface ShowProtocol<out T> {
    fun show(): T
}