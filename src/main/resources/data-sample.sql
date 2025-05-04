-- user
insert into users (user_id, name, email, created_at, updated_at)
values (1, 'username1', 'username1@email.com', '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into users (user_id, name, email, created_at, updated_at)
values (2, 'username2', 'username2@email.com', '2025-01-01 00:00:00', '2025-01-01 00:00:00');

-- product
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (1, 'product1', 'product description', 10000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (2, 'product2', 'product description', 20000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (3, 'product3', 'product description', 30000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (4, 'product4', 'product description', 40000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (5, 'product5', 'product description', 50000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');

-- product_stock
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (1, 100, 1, '2025-01-01 00:00:00');
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (2, 100, 2, '2025-01-01 00:00:00');
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (3, 100, 3, '2025-01-01 00:00:00');
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (4, 100, 4, '2025-01-01 00:00:00');
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (5, 100, 5, '2025-01-01 00:00:00');

--  coupon
insert into coupon(coupon_id, name, description, discount_type, percent, discount_amount, issue_start_at, issue_end_at,
                   max_discount_amount, min_order_amount, created_at, updated_at)
values (1, '10% 할인', '전체 금액 10% 할인 쿠폰', 'PERCENT_DISCOUNT', 0.1, null, '2025-01-01 00:00:00', '2026-01-01 00:00:00',
        10000, 50000, '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into coupon(coupon_id, name, description, discount_type, percent, discount_amount, issue_start_at, issue_end_at,
                   max_discount_amount, min_order_amount, created_at, updated_at)
values (2, '5000원 할인', '전체 금액 5000원 할인 쿠폰', 'AMOUNT_DISCOUNT', null, 5000, '2025-01-01 00:00:00', '2026-01-01 00:00:00',
        10000, 50000, '2025-01-01 00:00:00', '2025-01-01 00:00:00');

-- user_coupon
insert into user_coupon (user_coupon_id, coupon_id, user_id, created_at, updated_at)
values (1, 1, 1, '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into user_coupon (user_coupon_id, coupon_id, user_id, created_at, updated_at)
values (2, 1, 2, '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into user_coupon (user_coupon_id, coupon_id, user_id, created_at, updated_at)
values (3, 2, 2, '2025-01-01 00:00:00', '2025-01-01 00:00:00');

