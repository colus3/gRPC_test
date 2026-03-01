package com.test.grpc_test.server.domain

import com.querydsl.jpa.impl.JPAQueryFactory
import com.test.grpc_test.server.domain.QBookEntity.bookEntity
import org.springframework.stereotype.Repository

@Repository
class BookQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun findByGenre(genre: String): List<BookEntity> =
        queryFactory
            .selectFrom(bookEntity)
            .where(if (genre.isBlank()) null else bookEntity.genre.equalsIgnoreCase(genre))
            .fetch()

    fun search(query: String, maxResults: Int): List<BookEntity> =
        queryFactory
            .selectFrom(bookEntity)
            .where(
                bookEntity.title.containsIgnoreCase(query)
                    .or(bookEntity.author.containsIgnoreCase(query))
            )
            .limit(if (maxResults <= 0) 10L else maxResults.toLong())
            .fetch()
}