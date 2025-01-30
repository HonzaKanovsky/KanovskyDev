create schema portfolio_tracker;

use portfolio_tracker;
create table crypto
(
    id     bigint auto_increment
        primary key,
    symbol varchar(50)  not null,
    name   varchar(255) not null
);

create table historisation_crypto_price
(
    id         bigint auto_increment
        primary key,
    timestamp  date           not null,
    price      decimal(18, 8) not null,
    market_cap bigint         null,
    crypto_id  bigint         not null,
    constraint FK9txtxkx7bm56kr0vxkjciked
        foreign key (crypto_id) references crypto (id)
);

create table user
(
    id       bigint auto_increment
        primary key,
    username varchar(255) not null,
    password varchar(255) not null,
    email    varchar(255) not null,
    constraint email
        unique (email),
    constraint username
        unique (username)
);

create table portfolio_entry
(
    id        bigint auto_increment
        primary key,
    userID    bigint                             not null,
    cryptoID  bigint                             not null,
    amount    decimal(18, 4)                     not null,
    timestamp datetime default CURRENT_TIMESTAMP not null,
    constraint portfolio_entry_ibfk_1
        foreign key (userID) references user (id)
            on delete cascade,
    constraint portfolio_entry_ibfk_2
        foreign key (cryptoID) references crypto (id)
            on delete cascade
);

create index idx_cryptoID
    on portfolio_entry (cryptoID);

create index idx_userID
    on portfolio_entry (userID);

