package com.ubb.alexandrustoica.reporter.domain

interface Repository<T> {
    fun getAllDataFromRepository(): List<T>
    fun addAllFrom(list: List<T>): Repository<T>
    fun currentPage(): Int
}