create table product_comment
(
    id         bigint generated always as identity
        constraint product_comment_pk
            primary key,
    product_id bigint        not null
        constraint product_comment_products_id_fk
            references products,
    user_id    bigint        not null
        constraint product_comment_users_id_fk
            references users,
    comment    varchar(1000) not null
);