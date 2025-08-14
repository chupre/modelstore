create table orders
(
    id          bigint generated always as identity
        constraint orders_pk
            primary key,
    customer_id bigint                    not null
        constraint orders___fk
            references users,
    status      varchar(20)               not null,
    total_price decimal(10, 2)            not null,
    created_at  timestamptz default now() not null
);

create table order_items
(
    id         bigint generated always as identity
        constraint order_items_pk
            primary key,
    order_id   bigint         not null
        constraint order_items_orders_id_fk
            references orders (id),
    product_id bigint         not null
        constraint order_items_products_id_fk
            references products,
    price      decimal(10, 2) not null
);