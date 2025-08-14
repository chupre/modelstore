create table sellers
(
    id                 bigint generated always as identity
        constraint sellers_pk
            primary key,
    user_id            bigint      not null
        constraint sellers_users_id_fk
            references users,
    payout_method      varchar(20) not null,
    payout_destination varchar(64) not null
);