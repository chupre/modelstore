-- 20 product comments
INSERT INTO product_comments (product_id, user_id, comment, created_at) VALUES
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 0), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), 'Amazing model, love the details!', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 1), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), 'Great texture work!', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 2), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), 'Perfect for my project.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 3), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7), 'Very realistic!', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 4), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8), 'Nice and clean topology.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 5), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9), 'Exactly what I needed.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 6), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), 'Good for low-poly scenes.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 7), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), 'Stylized very well!', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 8), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), 'High-quality mesh.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 9), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7), 'Excellent detail on textures.', NOW()),

                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 0), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8), 'Loved the lighting setup.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 1), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9), 'Very useful asset.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 2), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), 'Easy to import into my scene.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 3), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), 'Looks fantastic!', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 4), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6), 'Exactly as advertised.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 5), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7), 'Very happy with this purchase.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 6), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8), 'Top-notch model.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 7), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9), 'Great detail, love it!', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 8), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4), 'Perfect for my animation.', NOW()),
                                                                            ((SELECT id FROM products ORDER BY id LIMIT 1 OFFSET 9), (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5), 'Highly recommended!', NOW());
