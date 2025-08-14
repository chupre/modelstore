alter table sellers
    add constraint sellers_pk_2
        unique (user_id);
