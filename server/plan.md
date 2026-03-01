# Server Module Plan

## 개요

- **역할**: gRPC 서비스 구현체를 제공하는 서버 애플리케이션
- **포트**: HTTP 8081 (Tomcat 위에서 HTTP + gRPC 동시 처리)
- **스타터**: `spring-grpc-server-web-spring-boot-starter`
  - Tomcat 서블릿 컨테이너 내에서 gRPC over HTTP/1.1 처리
  - 별도 Netty 서버 불필요

---

## 패키지 구조

```
com.test.grpc_test.server
├── ServerApplication.kt          ← Spring Boot 엔트리포인트
├── service/
│   └── BookGrpcService.kt        ← @Service, gRPC RPC 구현
└── domain/
    └── BookRepository.kt         ← in-memory 책 데이터 저장소
```

---

## Proto 서비스 (api 모듈에 정의)

```protobuf
service BookService {
  rpc GetBook(GetBookRequest) returns (GetBookResponse);      // Unary
  rpc ListBooks(ListBooksRequest) returns (stream Book);      // Server Streaming
  rpc SearchBooks(SearchBooksRequest) returns (stream Book);  // Server Streaming
}
```

---

## 주요 구현 포인트

### BookGrpcService.kt

- `BookServiceGrpc.BookServiceImplBase` 상속
- `@Service` 어노테이션으로 등록 — `BindableService` 구현체는 `@Service`만으로 자동 감지됨
  - `@GrpcService`(`org.springframework.grpc.server.service.GrpcService`)는 **선택적** — 서비스별 인터셉터 지정이 필요할 때만 사용
- `StreamObserver` 기반으로 응답 전송
  - Unary: `onNext(response)` → `onCompleted()`
  - Streaming: 루프로 `onNext(item)` 반복 → `onCompleted()`

### BookRepository.kt

- in-memory `List<Book>` 으로 데이터 제공 (DB 불필요)
- 제공 메서드:
  - `findById(id: String): Book?`
  - `findByGenre(genre: String): List<Book>` (빈 문자열이면 전체 반환)
  - `search(query: String, maxResults: Int): List<Book>` (title/author 키워드 검색)

---

## application.yml

```yaml
spring:
  application:
    name: grpc-book-server
server:
  port: 8081
```

---

## 빌드 의존성

```groovy
implementation project(':api')
implementation 'org.springframework.grpc:spring-grpc-server-web-spring-boot-starter'
implementation 'io.grpc:grpc-services'
```

---

## 실행

```bash
./gradlew :server:bootRun
```

서버 시작 후 포트 8081에서 HTTP 요청과 gRPC 요청을 모두 처리.
