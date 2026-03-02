package com.test.grpc_test.server.service

import com.test.grpc_test.api.book.Book
import com.test.grpc_test.api.book.BookServiceGrpc
import com.test.grpc_test.api.book.GetBookRequest
import com.test.grpc_test.api.book.GetBookResponse
import com.test.grpc_test.api.book.ListBooksRequest
import com.test.grpc_test.api.book.SearchBooksRequest
import com.test.grpc_test.server.domain.BookEntity
import com.test.grpc_test.server.domain.BookRepository
import com.test.grpc_test.server.interceptor.LoggingInterceptor
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import org.springframework.grpc.server.service.GrpcService

@GrpcService(interceptors = [LoggingInterceptor::class])
class BookGrpcService(
    private val bookRepository: BookRepository
) : BookServiceGrpc.BookServiceImplBase() {

    private val log = LoggerFactory.getLogger(BookGrpcService::class.java)

    // ── Unary RPC ────────────────────────────────────────────────────────────
    override fun getBook(
        request: GetBookRequest,
        responseObserver: StreamObserver<GetBookResponse>
    ) {
        log.debug("GetBook: id={}", request.id)

        val book = bookRepository.findById(request.id)
        val response = GetBookResponse.newBuilder()
            .setFound(book != null)
            .apply { book?.let { setBook(it.toProto()) } }
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    // ── Server Streaming RPC ─────────────────────────────────────────────────
    override fun listBooks(
        request: ListBooksRequest,
        responseObserver: StreamObserver<Book>
    ) {
        val pageSize = if (request.pageSize <= 0) 20 else request.pageSize
        log.debug("ListBooks: genre={}, pageSize={}", request.genre, pageSize)

        bookRepository.findByGenre(request.genre)
            .take(pageSize)
            .forEach { book ->
                responseObserver.onNext(book.toProto())
            }
        responseObserver.onCompleted()
    }

    // ── Server Streaming RPC ─────────────────────────────────────────────────
    override fun searchBooks(
        request: SearchBooksRequest,
        responseObserver: StreamObserver<Book>
    ) {
        log.debug("SearchBooks: query={}, maxResults={}", request.query, request.maxResults)

        bookRepository.search(request.query, request.maxResults)
            .forEach { book ->
                responseObserver.onNext(book.toProto())
            }
        responseObserver.onCompleted()
    }
}

private fun BookEntity.toProto(): Book = Book.newBuilder()
    .setId(id)
    .setTitle(title)
    .setAuthor(author)
    .setIsbn(isbn)
    .setYear(year)
    .setGenre(genre)
    .setPrice(price)
    .build()
