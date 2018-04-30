create table test1 (
    id serial,
    firstname varchar(20) NOT NULL,
    lastname varchar(20) NOT NULL,
    email varchar(60) NOT NULL,
    password varchar(60) NOT NULL,
    follows integer[],
    follower integer[]
);