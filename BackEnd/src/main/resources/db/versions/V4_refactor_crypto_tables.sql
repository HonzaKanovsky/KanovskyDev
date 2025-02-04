alter table crypto
    drop column price;

alter table crypto
    drop column market_cap;

alter table historisation_crypto_price
    add column market_cap bigint;