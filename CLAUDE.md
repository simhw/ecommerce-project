# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 개요

상품 주문/결제, 쿠폰 발급, 인기 상품 조회 API를 제공하는 학습용 Spring Boot 프로젝트.
과제의 핵심은 재고·잔액의 동시성 정합성과 다중 인스턴스 환경에서의 동작이다 (`docs/요구사항.md`).

## 스택

- Kotlin 1.9 + Java 21 혼용, Spring Boot 3.4, Gradle 8.13 (Kotlin DSL)
- JPA + QueryDSL 5 (jakarta), MapStruct, Lombok (kapt)
- MySQL(`localhost:3307`), Redis(`127.0.0.1:6379`, Redisson — 주소는 `RedissonConfig` 에 하드코딩) — `docker-compose.yml`
- 테스트: JUnit 5, MockK, `@SpringBootTest`

## 명령어

```bash
docker compose up -d                                   # MySQL + Redis (@SpringBootTest 테스트, 앱 실행에 필요)
./gradlew compileTestKotlin                            # 컴파일만 확인 (kapt 포함, 가장 빠른 검증)
./gradlew test                                         # 전체 테스트 (docker compose 필요)
./gradlew test --tests 'com.ecommerce.account.ChargeBalanceServiceTest'   # 단일 클래스
./gradlew test --tests '*PayOrderServiceTest.*잔고가 부족*'   # 단일 메서드 (한글 이름은 와일드카드로 매칭)
./gradlew bootRun                                      # Swagger UI: /swagger-ui.html
```

- 린터/포매터는 설정되어 있지 않다.
- 인프라 없이 도는 테스트는 MockK 단위 테스트뿐이다: `ChargeBalanceServiceTest`, `PayOrderServiceTest`, `IssueCouponServiceTest`, `PlaceOrderServiceTest`. 이름이 `*ServiceTest` 여도 `OrderQueryServiceTest` 는 `@SpringBootTest` 라 DB 가 필요하다.
- 실패 상세는 `build/reports/tests/test/index.html`, `build/test-results/test/*.xml`.

빌드 메모:

- `.java` 파일(엔티티, `Money` 등)이 `src/main/kotlin` 아래에 있어 `build.gradle.kts` 에서 Java source set 에 추가해 두었다.
- Lombok 은 kapt 와 javac 양쪽에서 처리한다 (`kapt("org.projectlombok:lombok")`, `keepJavacAnnotationProcessors = true`). MapStruct 가 Lombok builder/getter 를 쓰므로 둘 다 빼면 안 된다.
- IntelliJ 자체 빌더(JPS)는 Kotlin 1.9.25 와 최신 IDE 조합에서 `IncompatibleClassChangeError` 로 실패한다. IDE 에서도 "Build and run using: Gradle" 로 설정할 것.

## 아키텍처

도메인별 패키지(`com.ecommerce.{account,coupon,order,payment,product,user,usercoupon}`) + CQRS.

**Command (쓰기) — 헥사고날**

```
{domain}/command/
  adapter/in/web/            Controller, 요청/응답 DTO (같은 파일에 선언)
  adapter/in/event/          이벤트 핸들러
  adapter/out/persistence/   *PersistenceAdapter, *JpaRepository, *EntityMapper, entity/*Entity
  application/in/            *UseCase(인터페이스), *Command, *Info
  application/out/           *Port (Load*/Save* 또는 통합 Port)
  domain/model/              순수 도메인 모델 (JPA 의존 없음)
  domain/service/            UseCase 구현체 (@Service)
```

**Query (읽기) — 레이어드**: `{domain}/query/{ui,application,infra}`. 쓰기용 엔티티를 재사용하지 않고, 같은 테이블에 매핑한 조회 전용 JPA 모델 `*Data`(Java)를 따로 두고 QueryDSL 프로젝션으로 `*View` 를 바로 만든다.

규칙:

- 의존 방향: adapter → application → domain. 도메인 모델은 JPA/Spring 에 의존하지 않는다.
- 도메인 모델(Kotlin)과 JPA 엔티티(주로 Java + Lombok `@Builder`, 쿠폰 쪽은 Kotlin)는 분리하고 MapStruct `*EntityMapper`(`componentModel = "spring"`)로 변환한다. 어댑터는 조회 → 도메인 변환, 저장 시 도메인 → 엔티티 변환 후 `save` (더티 체킹에 기대지 않음).
- 서비스는 다른 도메인의 저장소에 `application/out` Port 로만 접근한다 (JpaRepository 직접 참조 없음). 단, 도메인 모델끼리는 직접 참조한다: `Order.place(UserCoupon)`, `Payment(order, user)` + `pay(account)`, `Account.user`.
- 비즈니스 규칙은 서비스가 아니라 도메인 객체에 둔다. 서비스는 로드 → 도메인 메서드 호출 → 저장 순서의 오케스트레이션만 한다.
- 금액은 `Money`, 수량은 `BigDecimal`.
- 예외는 커스텀 타입 없이 표준 예외 + 영문 메시지: 조회 실패·상태 검증은 `IllegalArgumentException`, `getIdOrThrow()`·`verifyActiveUser()` 는 `IllegalStateException`, 금액 연산은 `ArithmeticException`. 전역 예외 핸들러(`@ControllerAdvice`)는 없다.
- 사용자 식별은 인증 없이 `User-Id` 헤더(주문, 결제, 쿠폰 발급) 또는 경로 변수 `{userId}`(잔액 충전, 보유 쿠폰 조회).

### `Money` 의 동작 (테스트 기대값에 직접 영향)

- `plus` / `multiply` 는 인자가 0 이하이면 `ArithmeticException` — "0원 충전 거부"가 여기서 구현된다.
- `minus` 는 잔액보다 큰 금액이면 `ArithmeticException` — 잔고 부족 검증이 여기서 구현된다.
- `equals` 는 `doubleValue` 비교라 scale 이 달라도 같다.

### 주문 → 재고 차감 흐름

1. `PlaceOrderService` (`@Transactional`): 회원·상품 판매 상태 검증 → `Order.place(userCoupon)` (총액 계산, 쿠폰 적용, `ORDERED`) → 주문·쿠폰 저장 → `OrderPlacedEvent` 발행. 재고는 여기서 건드리지 않는다.
2. `DecreaseStockWithOrderPlacedEventHandler` (`@TransactionalEventListener(AFTER_COMPLETION)`): 같은 스레드에서 동기 실행된다(`@Async` 아님). `AFTER_COMMIT` 이 아니므로 주문 트랜잭션이 롤백돼도 호출된다.
3. `DecreaseStockService`: 상품 ID 들로 Redis 멀티락(`DistributeLock.multiLock`, wait 10s / lease 15s)을 잡은 뒤, 별도 빈 `DecreaseStockTransactionalExecutor` 의 `REQUIRES_NEW` 트랜잭션에서 차감한다. **락 → 트랜잭션 순서**(커밋 후 락 해제)를 지키기 위해 빈을 분리한 것이므로 합치지 말 것.
4. 차감 중 예외가 나면 핸들러가 `FailOrderUseCase` 로 주문을 `FAILED` 로 바꾼다 (보상 트랜잭션).

주의: `RedissonMultiLock` 은 락 획득에 실패하면 예외 없이 작업을 건너뛴다. 이 경우 재고는 차감되지 않고 주문은 `ORDERED` 로 남는다.

`decreaseStockWithPessimisticLock` 과 `StockJpaRepository.findForUpdateByProductId` 는 이전 설계(비관적 락)의 흔적으로 현재 호출되지 않는다. `docs/시퀀스다이어그램.md` 의 1)이 이전 설계, 2)가 현재 설계다.

### 결제

`PayOrderService`: `Payment.pay(account)` 가 `order.pay()`(→ `PAID`) → 결제 금액(총액 − 할인액) 계산 → `account.withdraw` 를 수행하고, 결제·계좌·주문을 각각 저장한다. 이 서비스에는 `@Transactional` 이 없고 잔액에 대한 락도 없다.

### 쿠폰

- `Coupon`(sealed: `AmountDiscountCoupon`, `PercentDiscountCoupon`)이 할인 금액 계산, `DiscountCondition`(sealed: `Period`/`Amount`/`None` + 합성 `All`/`Any`)이 할인 여부 판단을 맡는다 (`docs/Object-Oriented-Design.md`).
- 영속화: `coupon`, `discount_condition` 둘 다 SINGLE_TABLE 상속이고, 조건은 `parent_id` 자기참조 트리다. `CouponEntityMapper` 는 `@SubclassMapping`, `DiscountConditionEntityMapper` 는 직접 작성한 재귀 변환이다.
- 쿠폰 종류나 조건을 추가하려면 도메인 클래스 + 엔티티 서브클래스 + 매퍼(`@SubclassMapping` 또는 `when` 분기) 세 곳을 함께 고쳐야 한다.
- `UserCoupon.issue` 는 쿠폰 유효 기간만 검증한다. `UserCoupon.apply` 는 `UNUSED` → `USED` 로 바꾸고 할인액을 반환한다.

### 인기 상품

`RankingScheduler` 가 최근 3일 판매량 상위 30개를 QueryDSL 로 집계해 Redis ZSET `top:selling:products` 에 넣고, `GET /api/v1/products/best` 는 Redis 에서 ID 를 읽어 상품을 조회한다. 키 상수는 `RankingScheduler` 와 `ProductQueryRepository` 에 중복 선언되어 있다.

## 문서와 코드의 차이

`docs/` 는 설계 의도를 보는 용도이고, 스키마와 동작은 코드가 기준이다.

- `docs/ERD.md` 는 초기 설계다. 실제로는 재고가 `stock` 테이블로 분리됐고, `payment`·`discount_condition` 테이블이 있으며, 테이블명은 `users`·`orders`·`order_line_item` 이다. 스키마는 `ddl-auto: create-drop` 으로 엔티티에서 생성된다.
- 시퀀스 다이어그램은 재고 차감을 "비동기 후처리"로 그리지만 코드는 동기 실행이다.
- 요구사항 중 미구현: 잔액 조회 API, 선착순 쿠폰의 수량 제한(쿠폰에 재고 개념 없음), 결제 성공 시 데이터 플랫폼 전송. `CouponController` 는 빈 클래스다.

## 알려진 문제 (요청 없이 고치지 말고, 관련 작업 시 사용자에게 알릴 것)

- `@EnableScheduling` 이 없어 `RankingScheduler` 의 `@Scheduled` 가 실행되지 않는다.
- 상품 조회 모델 `ProductStockData` 는 `product_stock` 테이블에 매핑되어 있는데, 쓰기 쪽 `StockEntity` 는 `stock` 테이블이다. 상품 조회 API 는 실제 재고가 쓰이는 테이블을 읽지 않는다.
- `PercentDiscountCoupon.getDiscountAmount` 가 `discountAmount.isLessThan(discountAmount)` 로 자기 자신과 비교해 항상 `maxDiscountAmount` 를 반환한다.
- `CouponEntity.condition` 의 `@JoinColumn(name = "coupon_id")` 가 PK 컬럼명과 겹쳐 쿠폰 insert 가 `Field 'coupon_id' doesn't have a default value` 로 실패한다.
- 패키지 구조 불일치: `account`·`user`·`payment` 는 `command/` 계층이 없고, `account` 와 `coupon` 은 `application/port/{in,out}`, 나머지는 `application/{in,out}` 이다. Query 쪽도 `*Data` 위치가 `application`(order, coupon, usercoupon)과 `infra`(product)로 갈린다. 새 코드는 위 "아키텍처"의 구조를 따른다.
- 오타: `payment/applicaiton` 패키지, `UserCouponJapRepository`, `EcommerceProejctApplicationTests`.

## 테스트

- 위치: `src/test/kotlin/com/ecommerce/{domain}/`
- `*ServiceTest` — MockK 로 Port 를 목 처리한 단위 테스트 (`OrderQueryServiceTest` 는 예외, 통합 테스트)
- `*IntegrationTest` — `@SpringBootTest` + `@Transactional`, `EntityManager` 로 엔티티 직접 저장
- `*ConcurrencyTest` — `@SpringBootTest`, `ExecutorService` + `CountDownLatch`, 롤백·정리 없음
- 테스트 이름은 한글 백틱 문장, 본문은 `// given / when / then`
- `src/main/resources/data/*.sql` 은 어떤 테스트에서도 참조하지 않는다 (`@Sql` 사용 없음).

### 테스트 격리 문제

테스트들이 ID `1L`, `2L` 을 하드코딩하는데, Spring 컨텍스트(와 `create-drop` 스키마)는 테스트 클래스 간에 공유되고 동시성 테스트는 데이터를 정리하지 않는다. 그래서 **결과가 실행 순서에 따라 달라진다.** 예: `PlaceOrderServiceConcurrencyTest` 는 전체 실행에서는 실패하지만 단독 실행에서는 통과한다. 회귀 여부는 해당 클래스를 단독 실행해서 판단할 것.

### 실패하는 것으로 확인된 테스트

수치는 적지 않는다(실행 순서에 따라 달라짐). 아래 원인이 고쳐지면 해당 항목을 지울 것.

- `PlaceOrderServiceTest` — `ApplicationEventPublisher` 목에 `publishEvent` 스텁이 없고, 서비스가 더 이상 하지 않는 재고 차감을 검증한다.
- 쿠폰을 저장하는 통합 테스트 (`CalculateDiscountServiceIntegrationsTest`, `CouponEntityMapperTest`, `IssueCouponIntegrationTest`, `PlaceOrderServiceIntegrationTest`) — 위 `coupon_id` 매핑 문제.
- `ChargeBalanceIntegrationTest`, `PlaceOrderServiceIntegrationTest` — ID 하드코딩. 단독 실행 시 결과가 달라진다.
- `PlaceOrderServiceConcurrencyTest` — 격리 문제. 단독 실행 시 통과.
- `DecreaseStockServiceConcurrencyTest` — 단독 실행해도 실패. 재고 10 에 15건 차감 후 `-5` 를 기대하는데 실제 결과는 `0` 이다 (기대값이 잘못됨).

## Git

- 브랜치: `feat/*` → `dev` → `main` (PR 머지)
- 커밋: `feat:` / `fix:` / `refactor:` / `test:` / `docs:` + 한글 요약
