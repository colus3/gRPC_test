package com.test.grpc_test.server.config

import com.test.grpc_test.server.domain.BookEntity
import com.test.grpc_test.server.domain.BookJpaRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val bookJpaRepository: BookJpaRepository
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(DataInitializer::class.java)

    override fun run(args: ApplicationArguments) {
        if (bookJpaRepository.count() > 0) return

        val books = listOf(
            BookEntity("1", "Clean Code", "Robert C. Martin", "978-0132350884", 2008, "Programming", 35.99),
            BookEntity("2", "Domain-Driven Design", "Eric Evans", "978-0321125217", 2003, "Architecture", 55.00),
            BookEntity("3", "Designing Data-Intensive Applications", "Martin Kleppmann", "978-1449373320", 2017, "Architecture", 59.99),
            BookEntity("4", "The Pragmatic Programmer", "David Thomas", "978-0135957059", 2019, "Programming", 49.99),
            BookEntity("5", "Refactoring", "Martin Fowler", "978-0134757599", 2018, "Programming", 47.99),
            BookEntity("6", "Kubernetes in Action", "Marko Luksa", "978-1617293726", 2018, "DevOps", 59.99),
            BookEntity("7", "Site Reliability Engineering", "Niall Richard Murphy", "978-1491929124", 2016, "DevOps", 45.00),
            BookEntity("8", "Clean Architecture", "Robert C. Martin", "978-0134494166", 2017, "Architecture", 39.99),
        )

        bookJpaRepository.saveAll(books)
        log.info("Initialized {} books into database", books.size)
    }
}