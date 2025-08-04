INSERT INTO products (title, description, price, previewimage, file, owner_id, category_id, createdat) VALUES
           ('Sci-Fi Spaceship', 'A detailed 3D model of a futuristic spaceship.', 19.99, 'spaceship.png', 'spaceship.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 0),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 0),
            CURRENT_DATE),

           ('Medieval Castle', 'High-resolution 3D model of a medieval castle.', 24.99, 'castle.jpg', 'castle.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 1),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 1),
            CURRENT_DATE),

           ('Modern Car', 'Low-poly model of a modern sports car.', 9.99, 'car.png', 'car.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 2),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 2),
            CURRENT_DATE),

           ('Fantasy Sword', 'Stylized sword model suitable for RPG games.', 14.50, 'sword.png', 'sword.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 3),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 0),
            CURRENT_DATE),

           ('Cartoon House', 'Colorful house for cartoon-style environments.', 12.75, 'house.jpg', 'house.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 4),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 1),
            CURRENT_DATE),

           ('Alien Creature', 'Rigged alien creature with texture.', 29.00, 'alien.png', 'alien.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 5),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 2),
            CURRENT_DATE),

           ('Low-poly Trees', 'Pack of 10 low-poly tree models.', 8.99, 'trees.jpg', 'trees.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 6),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 0),
            CURRENT_DATE),

           ('Sci-Fi Crate', 'Stylized sci-fi crate with emissive textures.', 5.49, 'crate.png', 'crate.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 7),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 1),
            CURRENT_DATE),

           ('Medieval Sword', 'Realistic medieval sword model.', 11.20, 'medieval_sword.jpg', 'medieval_sword.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 8),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 2),
            CURRENT_DATE),

           ('Space Rover', 'NASA-inspired space rover model with wheels.', 17.80, 'rover.jpg', 'rover.obj',
            (SELECT id FROM users ORDER BY id LIMIT 1 OFFSET 9),
            (SELECT id FROM categories ORDER BY id LIMIT 1 OFFSET 0),
            CURRENT_DATE);
