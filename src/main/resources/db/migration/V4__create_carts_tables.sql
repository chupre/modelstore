-- Enable the uuid-ossp extension (only once per DB)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create carts table
CREATE TABLE carts (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       user_id BIGINT NOT NULL REFERENCES users(id),
                       created_at DATE NOT NULL DEFAULT NOW()
);

CREATE TABLE cart_items (
                            id BIGSERIAL PRIMARY KEY,
                            cart_id UUID NOT NULL REFERENCES carts(id),
                            product_id BIGINT NOT NULL REFERENCES products(id)
);