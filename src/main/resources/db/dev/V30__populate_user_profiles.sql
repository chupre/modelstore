-- User profiles population script
-- Avatar placeholder (same as products): "placeholder.png"

INSERT INTO user_profiles (user_id, name, avatar_url, bio) VALUES
                                                               ((SELECT id FROM users WHERE username = 'admin1'),  'admin1',  'placeholder.png', 'Admin of the ModelStore platform.'),

                                                               ((SELECT id FROM users WHERE username = 'seller1'), 'seller1', 'placeholder.png', '3D artist creating futuristic models.'),
                                                               ((SELECT id FROM users WHERE username = 'seller2'), 'seller2', 'placeholder.png', 'Designer focusing on medieval assets.'),
                                                               ((SELECT id FROM users WHERE username = 'seller3'), 'seller3', 'placeholder.png', 'Low-poly and stylized 3D model specialist.'),

                                                               ((SELECT id FROM users WHERE username = 'buyer1'),  'buyer1',  'placeholder.png', 'Regular buyer interested in sci-fi assets.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer2'),  'buyer2',  'placeholder.png', 'Loves medieval fantasy models.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer3'),  'buyer3',  'placeholder.png', 'Prefers modern architecture assets.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer4'),  'buyer4',  'placeholder.png', 'Enjoys cartoon and stylized packs.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer5'),  'buyer5',  'placeholder.png', 'Collector of realistic weapon models.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer6'),  'buyer6',  'placeholder.png', 'Interested in sci-fi vehicles.');
