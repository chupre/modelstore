-- 20 product comment likes
INSERT INTO product_comment_likes (comment_id, user_id, created_at) VALUES
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 0), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 1), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 2), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 3), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 4), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 5), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 6), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 7), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 8), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 9), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8), NOW()),

                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 10), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 11), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 12), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 13), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 14), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 15), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 16), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 17), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 18), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), NOW()),
                                                                        ((SELECT id FROM product_comments ORDER BY id LIMIT 1 OFFSET 19), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), NOW());
