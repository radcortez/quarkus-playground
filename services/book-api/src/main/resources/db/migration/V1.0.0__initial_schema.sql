create sequence id start with 1 increment by 1;

create table Book
(
    id     bigint not null,
    author varchar(255),
    genre  varchar(255),
    isbn   varchar(255),
    title  varchar(255),
    publishYear   integer,
    primary key (id)
)
