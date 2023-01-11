CREATE TABLE car
(
    id    bigserial PRIMARY KEY,
    brand varchar(255),
    model varchar(255),
    price numeric
);


CREATE TABLE human
(
    id             bigserial PRIMARY KEY,
    name           varchar(255),
    age            integer,
    driver_license boolean,
    car_id         bigint,

    FOREIGN KEY (car_id) REFERENCES car (id)
);
