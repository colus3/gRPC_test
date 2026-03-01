plugins {
    id("org.jetbrains.kotlin.jvm")           version "2.2.21" apply false
    id("org.jetbrains.kotlin.plugin.spring") version "2.2.21" apply false
    id("org.jetbrains.kotlin.plugin.jpa")    version "2.2.21" apply false
    id("org.jetbrains.kotlin.kapt")          version "2.2.21" apply false
    id("org.springframework.boot")           version "4.0.3"  apply false
    id("io.spring.dependency-management")    version "1.1.7"  apply false
    id("com.google.protobuf")                version "0.9.5"  apply false
}

val springGrpcVersion by extra("1.0.2")

subprojects {
    group   = "com.test"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    plugins.withId("org.jetbrains.kotlin.jvm") {
        configure<JavaPluginExtension> {
            toolchain {
                languageVersion = JavaLanguageVersion.of(21)
            }
        }
        configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
            compilerOptions {
                freeCompilerArgs.addAll(
                    "-Xjsr305=strict",
                    "-Xannotation-default-target=param-property"
                )
            }
        }
        tasks.named<Test>("test") {
            useJUnitPlatform()
        }
    }

    plugins.withId("io.spring.dependency-management") {
        configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
            imports {
                mavenBom("org.springframework.grpc:spring-grpc-dependencies:$springGrpcVersion")
            }
        }
    }
}