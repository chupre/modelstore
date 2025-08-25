-- 20 product likes
INSERT INTO product_likes (product_id, user_id, created_at) VALUES
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 0), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 1), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 2), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 3), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 4), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 5), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 6), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 7), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 8), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 9), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7), NOW()),

                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 0), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 1), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 2), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 3), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 4), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 5), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 6), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 7), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 8), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), NOW()),
                                                                ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 9), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), NOW());
