create table product_comment_likes
(
    id         bigint generated always as identity
        constraint product_comment_like_pk
            primary key,
    comment_id bigint                                 not null
        constraint product_comment_like_comment_id_fk
            references product_comments,
    user_id    bigint                                 not null
        constraint product_comment_like_users_id_fk
            references users,
    created_at timestamp with time zone default now() not null,
    constraint product_comment_like_pk_2
        unique (user_id, comment_id)
);