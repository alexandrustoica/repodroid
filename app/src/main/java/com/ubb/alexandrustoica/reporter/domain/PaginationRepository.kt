package com.ubb.alexandrustoica.reporter.domain

import java.util.concurrent.atomic.AtomicInteger

data class PaginationRepository<T>(
        private val data: List<T> = listOf(),
        private val currentPage: AtomicInteger = AtomicInteger(0)) :
        Repository<T> {
    override fun addAllFrom(list: List<T>): Repository<T> =
            PaginationRepository(data + list, currentPage)
    override fun currentPage(): Int = currentPage.getAndIncrement()
    override fun getAllDataFromRepository(): List<T> = data
}