DROP TABLE IF EXISTS credit_card;

CREATE TABLE credit_card(
    id bigint not null auto_increment,
    cvv varchar(30),
    expiration_date varchar(30),
    PRIMARY KEY (id)
);