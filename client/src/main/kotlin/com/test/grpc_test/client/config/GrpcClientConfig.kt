package com.test.grpc_test.client.config

import com.test.grpc_test.api.book.BookServiceGrpc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.grpc.client.GrpcChannelFactory

@Configuration
class GrpcClientConfig {

    @Bean
    fun bookServiceBlockingStub(channels: GrpcChannelFactory): BookServiceGrpc.BookServiceBlockingStub  =
        BookServiceGrpc.newBlockingStub(channels.createChannel("book-server"))
}
