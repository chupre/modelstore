create table password_reset_tokens
(
    id         bigint generated always as identity
        constraint password_reset_tokens_pk
            primary key,
    user_id    bigint      not null
        constraint password_reset_tokens_users_id_fk
            references users,
    token_hash varchar(64) not null
        constraint password_reset_tokens_uk
            unique,
    expires_at timestamptz not null
);