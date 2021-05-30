drop table if exists hibernate_sequence;
drop table if exists Ticket;
create table hibernate_sequence (next_val bigint) engine=InnoDB;
insert into hibernate_sequence values ( 1 );
create table Ticket (id bigint not null, ticketClass varchar(255), ticketDiscount varchar(255), train tinyblob, primary key (id)) engine=InnoDB;
