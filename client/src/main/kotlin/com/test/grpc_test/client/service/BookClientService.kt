package com.test.grpc_test.client.service

import com.test.grpc_test.api.book.Book
import com.test.grpc_test.api.book.BookServiceGrpc
import com.test.grpc_test.api.book.GetBookRequest
import com.test.grpc_test.api.book.GetBookResponse
import com.test.grpc_test.api.book.ListBooksRequest
import com.test.grpc_test.api.book.SearchBooksRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BookClientService(
    private val blockingStub: BookServiceGrpc.BookServiceBlockingStub
) {
    private val log = LoggerFactory.getLogger(BookClientService::class.java)

    fun getBook(id: String): GetBookResponse {
        log.debug("getBook: id={}", id)
        return blockingStub.getBook(
            GetBookRequest.newBuilder().setId(id).build()
        )
    }

    fun listBooks(genre: String = "", pageSize: Int = 20): List<Book> {
        log.debug("listBooks: genre={}, pageSize={}", genre, pageSize)
        val request = ListBooksRequest.newBuilder()
            .setGenre(genre)
            .setPageSize(pageSize)
            .build()
        return blockingStub.listBooks(request).asSequence().toList()
    }

    fun searchBooks(query: String, maxResults: Int = 10): List<Book> {
        log.debug("searchBooks: query={}, maxResults={}", query, maxResults)
        val request = SearchBooksRequest.newBuilder()
            .setQuery(query)
            .setMaxResults(maxResults)
            .build()
        return blockingStub.searchBooks(request).asSequence().toList()
    }
}
