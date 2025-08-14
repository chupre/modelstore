create table user_profiles
(
    id        bigint generated always as identity
        constraint user_profile_pk
            primary key,
    user_id   bigint      not null
        constraint user_profile_users_id_fk
            references users,
    name      varchar(50) not null
        constraint user_profile_pk_2
            unique,
    avatarUrl varchar(255),
    bio       varchar(500)
);

