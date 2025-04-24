drop table if exists `product`;

create table `product`
(
    product_id  bigint not null auto_increment,
    name        varchar(255),
    status      enum ('SELL','SOLD_OUT'),
    price_amount      numeric(38, 2),
    description varchar(255),
    created_at  timestamp(6),
    deleted_at  timestamp(6),
    updated_at  timestamp(6),
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