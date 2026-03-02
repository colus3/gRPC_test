package com.test.grpc_test.server.domain

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class BookRepository(
    private val jpaRepository: BookJpaRepository,
    private val queryRepository: BookQueryRepository
) {
    fun findById(id: String): BookEntity? =
        jpaRepository.findByIdOrNull(id)

    fun findByGenre(genre: String): List<BookEntity> =
        queryRepository.findByGenre(genre)

    fun search(query: String, maxResults: Int): List<BookEntity> =
        queryRepository.search(query, maxResults)
}