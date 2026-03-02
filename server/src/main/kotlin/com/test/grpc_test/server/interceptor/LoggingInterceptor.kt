package com.test.grpc_test.server.interceptor

import io.grpc.ForwardingServerCall.SimpleForwardingServerCall
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

// 서비스별 인터셉터 - @GrpcService(interceptors = [...]) 로 명시적 등록
@Component
class LoggingInterceptor : ServerInterceptor {

    private val log = LoggerFactory.getLogger(LoggingInterceptor::class.java)

    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val method = call.methodDescriptor.fullMethodName
        log.info("[Service] gRPC call started: {}", method)

        val loggingCall = object : SimpleForwardingServerCall<ReqT, RespT>(call) {
            override fun close(status: Status, trailers: Metadata) {
                if (status.isOk) {
                    log.info("[Service] gRPC call completed: {} [{}]", method, status.code)
                } else {
                    log.warn("[Service] gRPC call failed: {} [{} - {}]", method, status.code, status.description)
                }
                super.close(status, trailers)
            }
        }

        return next.startCall(loggingCall, headers)
    }
}