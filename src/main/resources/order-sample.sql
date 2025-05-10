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
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (6, 'product6', 'product description', 10000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (7, 'product7', 'product description', 20000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (8, 'product8', 'product description', 30000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (9, 'product9', 'product description', 40000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');
insert into product (product_id, name, description, price_amount, status, created_at, updated_at)
values (10, 'product10', 'product description', 50000, 'SELL', '2025-01-01 00:00:00', '2025-01-01 00:00:00');

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
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (6, 100, 6, '2025-01-01 00:00:00');
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (7, 100, 7, '2025-01-01 00:00:00');
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (8, 100, 8, '2025-01-01 00:00:00');
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (9, 100, 9, '2025-01-01 00:00:00');
insert into product_stock (product_stock_id, stock_value, product_id, updated_at)
values (10, 100, 10, '2025-01-01 00:00:00');
--  order