# 이커머스 서비스

## 프로젝트 구조

### 헥사고날 아키텍처

```
order
    |__ adapter
        |__ in.web 
            |__ OrderController
        |__ out.persistence
            |__ OrderEntity
            |__ OrderPersistenceAdapter
    |__ application 
        |__ in 
            |__ OrderUseCase
        |__ out
            |__ OrderLoadPort
            |__ OrderSavePort
    |__ domain 
        |__ model
            |__ Order 
            |__ OrderItem 
        |__ service 
            |__ PlaceOrderService 
```

## [ERD](docs%2FERD.md)

## [시퀀스다이어그램](docs%2F%EC%8B%9C%ED%80%80%EC%8A%A4%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8.md)