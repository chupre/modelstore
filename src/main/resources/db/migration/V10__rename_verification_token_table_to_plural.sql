alter table verification_token
    rename constraint verification_token_pk to verification_tokens_pk;

alter table verification_token
    rename constraint verification_token_users_id_fk to verification_tokens_users_id_fk;

alter table verification_token
    rename to verification_tokens;