create table city (
    id int8 not null,
    tag varchar(255),
    text varchar(255),
    person_id int8,
    region_id int8,
    primary key (id)
    );
create table message (
    id int8 not null,
    tag varchar(255),
    text varchar(255),
    user_id int8,
    primary key (id))
create table oblast (
    id int8 not null,
    name varchar(255),
    person_id int8,
    primary key (id))
create table person (
    id int8 not null,
    name varchar(255),
    partion varchar(255),
    surname varchar(255),
    type_person int4,
    primary key (id))
create table region (
    id int8 not null,
    name varchar(255),
    oblast_id int8,
    person_id int8,
    primary key (id))
create table street (
    id int8 not null,
    name varchar(255),
    city_id int8,
    primary key (id))
create table user_role (
    user_id int8 not null,
    roles varchar(255))
create table usr (
    id int8 not null,
    active boolean not null,
    chat_id int8,
    password varchar(255),
    support_admin boolean not null,
    username varchar(255), primary key (id))



alter table if exists city
    add constraint FKa9dcnfclsmvtoq4xbkg9ripu2
    foreign key (person_id) references oblast;
alter table if exists city
    add constraint FKsi0dkm9kk6dyuedmc0j18t770
    foreign key (region_id) references region;
alter table if exists message
    add constraint FK70bv6o4exfe3fbrho7nuotopf
    foreign key (user_id) references usr;
alter table if exists oblast
    add constraint FKpo6saub1s73tjdht6bhdkkmp0
    foreign key (person_id) references person;
alter table if exists region
    add constraint FKl82m4j51d2vi5nah9wb9bqcsx
    foreign key (oblast_id) references oblast;
alter table if exists region
    add constraint FKdibbj3rmnk504l7si14xd5tci
    foreign key (person_id) references person;
alter table if exists street
    add constraint FKacmccbd831lu9xq9plvwbc4y7
    foreign key (city_id) references city;
alter table if exists user_role
    add constraint FKfpm8swft53ulq2hl11yplpr5
    foreign key (user_id) references usr;