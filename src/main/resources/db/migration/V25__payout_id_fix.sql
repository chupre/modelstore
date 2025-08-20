alter table payouts
    alter column id add generated always as identity;