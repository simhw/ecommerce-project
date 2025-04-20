````mermaid
erDiagram
    USER {
        bigint user_id PK
        varchar(255) uesrname
        datetime(6) created_at
        datetime(6) updated_at
        datetime(6) deleted_at
    }

    ACCOUNT {
        bigint account_id PK
        int amount
        bigint user_id FK
        datetime(6) created_at
        datetime(6) updated_at
        datetime(6) deleted_at
    }

    PRODUCT {
        bigint product_id PK
        varchar(255) name
        varchar(255) price
        bigint stock
        datetime(6) created_at
        datetime(6) updated_at
        datetime(6) deleted_at
    }

    ORDER {
        bigint order_id PK
        int total_amount
        bigint user_id FK
        datetime(6) created_at
        datetime(6) updated_at
        datetime(6) deleted_at
    }

    ORDER_ITEM {
        int price
        int quantity
        int amount
        bigint order_id FK
        bigint product_id FK
    }

    COUPON {
        bigint coupon_id PK
        varchar(255) name
        varchar(10) discount_type
        int discount_value
        int stock
        int min_pay_amount
        int max_discount_amount
        datetime(6) use_start_at
        datetime(6) use_end_at
        datetime(6) created_at
        datetime(6) updated_at
        datetime(6) deleted_at
    }

    USER_COUPON {
        bigint user_coupon_id PK
        boolean is_used
        bigint user_id FK
        bigint coupon_id FK
        datetime(6) created_at
        datetime(6) updated_at
        datetime(6) deleted_at
    }

    USER ||--|| ACCOUNT: "has"
    USER ||--o{ ORDER: "places"
    ORDER ||--|{ ORDER_ITEM: "contains"
    ORDER_ITEM ||--|{ PRODUCT: "includes"
    USER ||--o{ USER_COUPON: "issues"
    COUPON ||--o{ USER_COUPON: "is assigned"
    ORDER ||--o{ USER_COUPON: "uses"

````