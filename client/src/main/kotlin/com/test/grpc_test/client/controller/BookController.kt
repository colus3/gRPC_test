package com.test.grpc_test.client.controller

import com.test.grpc_test.client.service.BookClientService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
    private val bookClientService: BookClientService
) {

    // GET /books/search?q=clean  →  gRPC SearchBooks (Server Streaming)
    // /search 가 /{id} 보다 먼저 선언되어야 경로 충돌 없음
    @GetMapping("/search")
    fun searchBooks(
        @RequestParam q: String,
        @RequestParam(defaultValue = "10") maxResults: Int
    ): List<Map<String, Any>> =
        bookClientService.searchBooks(q, maxResults).map { book ->
            mapOf(
                "id" to book.id,
                "title" to book.title,
                "author" to book.author,
                "genre" to book.genre,
                "price" to book.price
            )
        }

    // GET /books/{id}  →  gRPC GetBook (Unary)
    @GetMapping("/{id}")
    fun getBook(@PathVariable id: String): Map<String, Any> {
        val response = bookClientService.getBook(id)
        return if (response.found) {
            val book = response.book
            mapOf(
                "id" to book.id,
                "title" to book.title,
                "author" to book.author,
                "isbn" to book.isbn,
                "year" to book.year,
                "genre" to book.genre,
                "price" to book.price
            )
        } else {
            mapOf("error" to "Book not found", "id" to id)
        }
    }

    // GET /books?genre=Architecture  →  gRPC ListBooks (Server Streaming)
    @GetMapping
    fun listBooks(
        @RequestParam(defaultValue = "") genre: String,
        @RequestParam(defaultValue = "20") pageSize: Int
    ): List<Map<String, Any>> =
        bookClientService.listBooks(genre, pageSize).map { book ->
            mapOf(
                "id" to book.id,
                "title" to book.title,
                "author" to book.author,
                "genre" to book.genre,
                "price" to book.price
            )
        }
}
