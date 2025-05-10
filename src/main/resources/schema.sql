drop table if exists `users`;
create table `users`
(
    user_id    bigint not null auto_increment,
    email      varchar(255),
    name       varchar(255),
    created_at timestamp(6),
    deleted_at timestamp(6),
    updated_at timestamp(6),
    primary key (user_id)
);

drop table if exists `account`;
create table `account`
(
    account_id bigint not null auto_increment,
    balance    numeric(38, 2),
    user_id    bigint not null,
    created_at timestamp(6),
    deleted_at timestamp(6),
    updated_at timestamp(6),
    primary key (account_id)
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
    issue_start_at      timestamp(6),
    issue_end_at        timestamp(6),
    max_discount_amount numeric(38, 2),
    min_order_amount    numeric(38, 2),
    use_start_at        timestamp(6),
    use_end_at          timestamp(6),
    created_at          timestamp(6),
    deleted_at          timestamp(6),
    updated_at          timestamp(6),
    primary key (coupon_id)
);

drop table if exists `user_coupon`;
create table `user_coupon`
(
    user_coupon_id bigint not null auto_increment,
    user_id        bigint,
    coupon_id      bigint,
    status         enum ('CANCELLED','EXPIRED','UNUSED','USED'),
    created_at     timestamp(6),
    deleted_at     timestamp(6),
    updated_at     timestamp(6),
    primary key (user_coupon_id)
);

drop table if exists `orders`;
create table `orders`
(
    order_id               bigint not null auto_increment,
    order_number           varchar(255),
    user_id                bigint,
    shipping_street        varchar(255),
    shipping_city          varchar(255),
    total_amounts          numeric(38, 2),
    total_discount_amounts numeric(38, 2),
    status                 enum ('PENDING','ORDERED', 'PAID', 'CANCELED'),
    created_at             timestamp(6),
    deleted_at             timestamp(6),
    updated_at             timestamp(6),
    primary key (order_id)
);

drop table if exists `order_line_item`;
create table `order_line_item`
(
    order_line_item_id bigint not null auto_increment,
    order_id           bigint,
    user_id            bigint,
    item_price         numeric(38, 2),
    item_amount        numeric(38, 2),
    quantity           numeric(38, 2),
    product_id         bigint,
    primary key (order_line_item_id)
);
