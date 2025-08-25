-- 10 payouts
DO $$
    DECLARE
        commission NUMERIC := 0.05;
        statuses TEXT[] := ARRAY['PENDING', 'PAID', 'CANCELLED', 'FAILED'];
    BEGIN
        FOR i IN 0..9 LOOP
                INSERT INTO payouts (seller_id, order_id, amount, status, payment_id, created_at)
                VALUES (
                           (SELECT owner_id FROM products ORDER BY id LIMIT 1 OFFSET i),    -- seller of the product
                           (SELECT id FROM orders ORDER BY id LIMIT 1 OFFSET i),           -- matching order
                           (SELECT price FROM products ORDER BY id LIMIT 1 OFFSET i) * (1 - commission), -- payout amount
                           statuses[1 + (i % array_length(statuses,1))],                  -- cycling statuses
                           gen_random_uuid()::text,
                           NOW()
                       );
            END LOOP;
    END $$;
