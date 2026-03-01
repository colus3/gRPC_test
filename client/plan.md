# Client Module Plan

## 개요

- **역할**: REST API를 노출하고 내부적으로 gRPC로 서버를 호출하는 클라이언트 애플리케이션
- **포트**: HTTP 8080
- **스타터**: `spring-grpc-spring-boot-starter` (GrpcChannelFactory 제공)
- **REST**: `spring-boot-starter-webmvc`

---

## 패키지 구조

```
com.test.grpc_test.client
├── ClientApplication.kt           ← Spring Boot 엔트리포인트
├── config/
│   └── GrpcClientConfig.kt        ← gRPC 채널 및 Stub Bean 등록
├── controller/
│   └── BookController.kt          ← REST endpoint
└── service/
    └── BookClientService.kt       ← gRPC 호출 래핑
```

---

## REST API 설계

| Method | Path | gRPC 매핑 | RPC 유형 |
|--------|------|-----------|----------|
| GET | `/books/{id}` | `GetBook` | Unary |
| GET | `/books?genre=&pageSize=` | `ListBooks` | Server Streaming |
| GET | `/books/search?q=&maxResults=` | `SearchBooks` | Server Streaming |

---

## 주요 구현 포인트

### GrpcClientConfig.kt

- `GrpcChannelFactory.createChannel("book-server")`로 채널 생성
- 채널 이름 `"book-server"`는 `application.yml`의 채널 설정 키와 일치해야 함
- `BookServiceGrpc.newBlockingStub(channel)` → Unary / Streaming 모두 처리 가능

### BookClientService.kt

- `BlockingStub` 사용
  - Unary: `blockingStub.getBook(request)`
  - Streaming: `blockingStub.listBooks(request)` → Iterator 반환 → `asSequence().toList()`

### BookController.kt

- 조회 결과를 `Map<String, Any>` 형태로 JSON 응답
- `@GetMapping("/search")` 경로가 `@GetMapping("/{id}")` 보다 먼저 매핑되도록 순서 주의

---

## application.yml

```yaml
spring:
  application:
    name: grpc-book-client
  grpc:
    client:
      channels:
        book-server:
          address: "http://localhost:8081"
          # spring-grpc-server-web 기반이므로 http:// 사용
          # 표준 Netty 서버라면 static://localhost:9090
server:
  port: 8080
```

---

## 빌드 의존성

```groovy
implementation project(':api')
implementation 'org.springframework.grpc:spring-grpc-spring-boot-starter'
implementation 'org.springframework.boot:spring-boot-starter-webmvc'
```

---

## 실행

```bash
./gradlew :client:bootRun
```

서버가 먼저 실행된 상태에서 클라이언트를 시작.

### 테스트 명령

```bash
# Unary - 특정 책 조회
curl http://localhost:8080/books/1

# Server Streaming - 장르별 목록
curl "http://localhost:8080/books?genre=Architecture"

# Server Streaming - 키워드 검색
curl "http://localhost:8080/books/search?q=clean"
```
