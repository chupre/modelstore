-- Create carts for buyers
DO $$
    BEGIN
        FOR i IN 0..5 LOOP  -- users 5-10
        INSERT INTO carts (user_id, created_at)
        VALUES (
                   (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4 + i),
                   NOW()
               );
            END LOOP;
    END $$;

-- Add cart items
DO $$
    DECLARE
        product_count INT;
        prod_id BIGINT;
        cart_uuid UUID;
    BEGIN
        FOR i IN 0..5 LOOP
                cart_uuid := (SELECT id FROM carts ORDER BY id LIMIT 1 OFFSET i);
                product_count := 1 + (random() * 3)::int;  -- 1 to 3 products per cart

                FOR j IN 0..(product_count - 1) LOOP
                        prod_id := (SELECT id FROM products ORDER BY id LIMIT 1 OFFSET ((i + j) % 10));
                        INSERT INTO cart_items (cart_id, product_id, is_selected)
                        VALUES (cart_uuid, prod_id, true);
                    END LOOP;
            END LOOP;
    END $$;
