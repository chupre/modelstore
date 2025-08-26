DO $$
    DECLARE
        img_placeholder TEXT := '/images/placeholder.png';
        file_placeholder TEXT := '/models/placeholder.obj';
    BEGIN
        INSERT INTO products (title, description, price, previewimage, file, owner_id, category_id, createdat) VALUES
                                                                                                                   ('Sci-Fi Spaceship', 'A detailed 3D model of a futuristic spaceship.', 19.99, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 0),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 0),
                                                                                                                    CURRENT_DATE),

                                                                                                                   ('Medieval Castle', 'High-resolution 3D model of a medieval castle.', 24.99, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 1),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 1),
                                                                                                                    CURRENT_DATE),

                                                                                                                   ('Modern Car', 'Low-poly model of a modern sports car.', 9.99, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 2),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 2),
                                                                                                                    CURRENT_DATE),

                                                                                                                   ('Fantasy Sword', 'Stylized sword model suitable for RPG games.', 14.50, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 3),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 0),
                                                                                                                    CURRENT_DATE),

                                                                                                                   ('Cartoon House', 'Colorful house for cartoon-style environments.', 12.75, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 0),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 1),
                                                                                                                    CURRENT_DATE),

                                                                                                                   ('Alien Creature', 'Rigged alien creature with texture.', 29.00, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 1),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 2),
                                                                                                                    CURRENT_DATE),

                                                                                                                   ('Low-poly Trees', 'Pack of 10 low-poly tree models.', 8.99, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 2),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 0),
                                                                                                                    CURRENT_DATE),

                                                                                                                   ('Sci-Fi Crate', 'Stylized sci-fi crate with emissive textures.', 5.49, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 3),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 1),
                                                                                                                    CURRENT_DATE),

                                                                                                                   ('Medieval Sword', 'Realistic medieval sword model.', 11.20, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 0),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 2),
                                                                                                                    CURRENT_DATE),

                                                                                                                   ('Space Rover', 'NASA-inspired space rover model with wheels.', 17.80, img_placeholder, file_placeholder,
                                                                                                                    (SELECT id FROM users WHERE username LIKE 'admin%' OR username LIKE 'seller%' ORDER BY id LIMIT 1 OFFSET 1),
                                                                                                                    (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 0),
                                                                                                                    CURRENT_DATE);
    END $$;
