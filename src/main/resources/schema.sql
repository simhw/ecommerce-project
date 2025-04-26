drop table if exists `users`;
create table users
(
    user_id    bigint not null auto_increment,
    email      varchar(255),
    name       varchar(255),
    created_at timestamp(6),
    deleted_at timestamp(6),
    updated_at timestamp(6),
    primary key (user_id)
);

drop table if exists `product`;
create table `product`
(
    product_id   bigint not null auto_increment,
    name         varchar(255),
    status       enum ('SELL','SOLD_OUT'),
    price_amount numeric(38, 2),
    description  varchar(255),
    created_at   timestamp(6),
    deleted_at   timestamp(6),
    updated_at   timestamp(6),
    primary key (product_id)
);

drop table if exists `product_stock`;
create table `product_stock`
(
    product_stock_id bigint not null auto_increment,
    stock_value      numeric(38, 2),
    product_id       bigint unique,
    updated_at       timestamp(6),
    primary key (product_stock_id)
);

drop table if exists `coupon`;
create table coupon
(
    coupon_id           bigint      not null auto_increment,
    name                varchar(255),
    description         varchar(255),
    discount_type       varchar(31) not null,
    discount_amount     numeric(38, 2),
    percent             numeric(38, 2),
    issue_end_at        timestamp(6),
    issue_start_at      timestamp(6),
    max_discount_amount numeric(38, 2),
    min_order_amount    numeric(38, 2),
    use_end_at          timestamp(6),
    use_start_at        timestamp(6),
    created_at          timestamp(6),
    deleted_at          timestamp(6),
    updated_at          timestamp(6),
    primary key (coupon_id)
);

drop table if exists `user_coupon`;
create table user_coupon
(
    user_coupon_id bigint not null auto_increment,
    user_id        bigint,
    coupon_id      bigint,
    created_at     timestamp(6),
    deleted_at     timestamp(6),
    updated_at     timestamp(6),
    primary key (user_coupon_id)
)