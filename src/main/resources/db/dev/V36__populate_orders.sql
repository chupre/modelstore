-- 10 orders
DO $$
    DECLARE
        statuses TEXT[] := ARRAY['PENDING', 'PAID', 'FAILED', 'CANCELLED'];
    BEGIN
        FOR i IN 0..9 LOOP
                INSERT INTO orders (customer_id, status, total_price, created_at, payment_id)
                VALUES (
                           (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4 + (i % 6)),  -- users 5-10
                           statuses[1 + (i % array_length(statuses,1))],                   -- cycling statuses
                           (SELECT price FROM products ORDER BY id LIMIT 1 OFFSET i),       -- use product price as total
                           NOW(),
                           gen_random_uuid()
                       );
            END LOOP;
    END $$;

-- 10 order items
DO $$
    BEGIN
        FOR i IN 0..9 LOOP
                INSERT INTO order_items (order_id, product_id, price)
                VALUES (
                           (SELECT id FROM orders ORDER BY id LIMIT 1 OFFSET i),
                           (SELECT id FROM products ORDER BY id LIMIT 1 OFFSET i),
                           (SELECT price FROM products ORDER BY id LIMIT 1 OFFSET i)
                       );
            END LOOP;
    END $$;
