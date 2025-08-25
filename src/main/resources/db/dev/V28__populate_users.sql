-- NOTE: Plaintext passwords for testing:
--   admin1  -> admin123
--   seller1 -> password123
--   seller2 -> password123
--   seller3 -> password123
--   buyer1  -> password123
--   buyer2  -> password123
--   buyer3  -> password123
--   buyer4  -> password123
--   buyer5  -> password123
--   buyer6  -> password123

INSERT INTO users (username, email, password, role, verified, createdat) VALUES
-- ADMIN
('admin1', 'admin1@example.com', '$2a$10$Ug4.k5kKWsOaouMR0tAXR.mRepismELFpghlNVqukGb01ANZYxYua', 'ADMIN', true, CURRENT_DATE),

-- SELLERS
('seller1', 'seller1@example.com', '$2a$10$fUiEuIHsQ5XpHG7pbvz8SuaqJvl2GRBhJZzcg.6l.ZmXDdyuZOkeC', 'SELLER', true, CURRENT_DATE),
('seller2', 'seller2@example.com', '$2a$10$fUiEuIHsQ5XpHG7pbvz8SuaqJvl2GRBhJZzcg.6l.ZmXDdyuZOkeC', 'SELLER', true, CURRENT_DATE),
('seller3', 'seller3@example.com', '$2a$10$fUiEuIHsQ5XpHG7pbvz8SuaqJvl2GRBhJZzcg.6l.ZmXDdyuZOkeC', 'SELLER', true, CURRENT_DATE),

-- BUYERS
('buyer1', 'buyer1@example.com', '$2a$10$fUiEuIHsQ5XpHG7pbvz8SuaqJvl2GRBhJZzcg.6l.ZmXDdyuZOkeC', 'BUYER', true, CURRENT_DATE),
('buyer2', 'buyer2@example.com', '$2a$10$fUiEuIHsQ5XpHG7pbvz8SuaqJvl2GRBhJZzcg.6l.ZmXDdyuZOkeC', 'BUYER', true, CURRENT_DATE),
('buyer3', 'buyer3@example.com', '$2a$10$fUiEuIHsQ5XpHG7pbvz8SuaqJvl2GRBhJZzcg.6l.ZmXDdyuZOkeC', 'BUYER', true, CURRENT_DATE),
('buyer4', 'buyer4@example.com', '$2a$10$fUiEuIHsQ5XpHG7pbvz8SuaqJvl2GRBhJZzcg.6l.ZmXDdyuZOkeC', 'BUYER', true, CURRENT_DATE),
('buyer5', 'buyer5@example.com', '$2a$10$fUiEuIHsQ5XpHG7pbvz8SuaqJvl2GRBhJZzcg.6l.ZmXDdyuZOkeC', 'BUYER', true, CURRENT_DATE),
('buyer6', 'buyer6@example.com', '$2a$10$fUiEuIHsQ5XpHG7pbvz8SuaqJvl2GRBhJZzcg.6l.ZmXDdyuZOkeC', 'BUYER', true, CURRENT_DATE);
