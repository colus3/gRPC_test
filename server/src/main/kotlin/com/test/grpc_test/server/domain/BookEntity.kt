package com.test.grpc_test.server.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "books")
class BookEntity(
    @Id
    val id: String,

    val title: String,

    val author: String,

    val isbn: String,

    @Column(name = "publish_year")
    val year: Int,

    val genre: String,

    val price: Double
)