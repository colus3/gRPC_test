package com.test.grpc_test.server.domain

import com.test.grpc_test.api.book.Book
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class BookRepository(
    private val jpaRepository: BookJpaRepository,
    private val queryRepository: BookQueryRepository
) {
    fun findById(id: String): Book? =
        jpaRepository.findByIdOrNull(id)?.toProto()

    fun findByGenre(genre: String): List<Book> =
        queryRepository.findByGenre(genre).map { it.toProto() }

    fun search(query: String, maxResults: Int): List<Book> =
        queryRepository.search(query, maxResults).map { it.toProto() }

    private fun BookEntity.toProto(): Book = Book.newBuilder()
        .setId(id)
        .setTitle(title)
        .setAuthor(author)
        .setIsbn(isbn)
        .setYear(year)
        .setGenre(genre)
        .setPrice(price)
        .build()
}