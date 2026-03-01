package com.test.grpc_test.server.domain

import org.springframework.data.jpa.repository.JpaRepository

interface BookJpaRepository : JpaRepository<BookEntity, String>