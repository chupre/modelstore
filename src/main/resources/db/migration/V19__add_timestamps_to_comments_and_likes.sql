alter table product_comment
    rename constraint product_comment_pk to product_comments_pk;

alter table product_comment
    rename constraint product_comment_products_id_fk to product_comments_products_id_fk;

alter table product_comment
    rename constraint product_comment_users_id_fk to product_comments_users_id_fk;

alter table product_comment
    rename to product_comments;

alter table product_comments
    add created_at timestamptz default now() not null;

alter table product_likes
    add created_at timestamptz default now() not null;

