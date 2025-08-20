alter table payouts
    alter column payment_id type uuid using payment_id::uuid;