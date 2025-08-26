-- User profiles population script
-- Avatar placeholder (same as products): "/images/placeholder.png"

INSERT INTO user_profiles (user_id, name, avatar_url, bio) VALUES
                                                               ((SELECT id FROM users WHERE username = 'admin1'),  'admin1',  '/images/placeholder.png', 'Admin of the ModelStore platform.'),

                                                               ((SELECT id FROM users WHERE username = 'seller1'), 'seller1', '/images/placeholder.png', '3D artist creating futuristic models.'),
                                                               ((SELECT id FROM users WHERE username = 'seller2'), 'seller2', '/images/placeholder.png', 'Designer focusing on medieval assets.'),
                                                               ((SELECT id FROM users WHERE username = 'seller3'), 'seller3', '/images/placeholder.png', 'Low-poly and stylized 3D model specialist.'),

                                                               ((SELECT id FROM users WHERE username = 'buyer1'),  'buyer1',  '/images/placeholder.png', 'Regular buyer interested in sci-fi assets.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer2'),  'buyer2',  '/images/placeholder.png', 'Loves medieval fantasy models.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer3'),  'buyer3',  '/images/placeholder.png', 'Prefers modern architecture assets.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer4'),  'buyer4',  '/images/placeholder.png', 'Enjoys cartoon and stylized packs.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer5'),  'buyer5',  '/images/placeholder.png', 'Collector of realistic weapon models.'),
                                                               ((SELECT id FROM users WHERE username = 'buyer6'),  'buyer6',  '/images/placeholder.png', 'Interested in sci-fi vehicles.');
