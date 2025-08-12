create table verification_token
(
    id         uuid default uuid_generate_v4() not null
        constraint verification_token_pk
            primary key,
    user_id    bigint                          not null
        constraint verification_token_users_id_fk
            references users,
    expires_at timestamptz                     not null
);