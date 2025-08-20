alter table payouts
    alter column payment_id type varchar(255) using payment_id::varchar(255);