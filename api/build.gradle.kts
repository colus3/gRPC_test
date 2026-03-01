plugins {
    id("org.jetbrains.kotlin.jvm")
    id("io.spring.dependency-management")
    id("com.google.protobuf")
}

description = "Shared protobuf API definitions and generated gRPC stubs"

dependencies {
    api("io.grpc:grpc-stub")
    api("io.grpc:grpc-protobuf")
    api("com.google.protobuf:protobuf-java")

    // 생성된 stub 클래스의 @Generated 어노테이션 처리
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
                create("grpc")
            }
        }
    }
}