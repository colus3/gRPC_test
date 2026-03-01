plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

description = "REST-to-gRPC client module"

dependencies {
    // 공유 stub 의존
    implementation(project(":api"))

    // gRPC 클라이언트 (GrpcChannelFactory 제공)
    implementation("org.springframework.grpc:spring-grpc-spring-boot-starter")

    // REST 레이어
    implementation("org.springframework.boot:spring-boot-starter-webmvc")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("tools.jackson.module:jackson-module-kotlin")

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}