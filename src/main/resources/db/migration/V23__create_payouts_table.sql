create table payouts
(
    id         bigint                    not null
        constraint payouts_pk
            primary key,
    seller_id  bigint                    not null
        constraint payouts_sellers_id_fk
            references sellers,
    order_id   bigint                    not null
        constraint payouts_orders_id_fk
            references orders,
    amount     numeric                   not null,
    status     varchar(20)               not null,
    payment_id varchar(255),
    created_at timestamptz default now() not null
);