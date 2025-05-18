# 이커머스 프로젝트

## 1. 주요 기능

[요구사항.md](docs%2F%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD.md)

- 상품 조회 API
- 주문 / 결제 API
- 인기 판매 상품 조회 API

## 2. 기술 스택

- Kotlin, Java, Spring Boot
- MySQL, Redis
- JPA, QueryDSL, Junit 등

## 3. [ERD](docs%2FERD.md)

## 4. 아키텍처

### Command

<details>
<summary>헥사고날 아키텍처</summary>

```
order
    |__ adapter
        |__ in.web 
            |__ OrderController.kt 
        |__ out.persistence
            |__ OrderEntity.java 
            |__ OrderPersistenceAdapter.kt
    |__ application 
        |__ in 
            |__ PlaceOrderUseCase.kt
        |__ out
            |__ OrderLoadPort.kt
            |__ OrderSavePort.kt
    |__ domain 
        |__ model
            |__ Order.kt 
        |__ service 
            |__ PlaceOrderService.kt 
```

</details>

### Query

<details>
<summary>레이어드 아키텍처</summary>

```
product 
    |__ controller   
        |__ ProductQueryController.kt 
    |__ application 
        |__ ProductQueryService.kt 
        |__ ProductView.kt  
    |__ infra  
        |__ ProductQueryRepository.kt
        |__ ProductData.java
        |__ StockData.java
        
```

</details>

## 5. [시퀀스다이어그램](docs%2F%EC%8B%9C%ED%80%80%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.md)