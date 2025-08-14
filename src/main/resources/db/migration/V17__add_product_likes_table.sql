create table product_likes
(
    id         bigint generated always as identity
        constraint product_likes_pk
            primary key,
    product_id bigint not null
        constraint product_likes_products_id_fk
            references products,
    user_id    bigint not null
        constraint product_likes_users_id_fk
            references users,
    constraint product_likes_pk_2
        unique (user_id, product_id)
);