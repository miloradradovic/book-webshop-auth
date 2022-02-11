drop table if exists user_roles;
drop table if exists users;

create table user_roles (
	user_id integer not null,
	roles varchar(255)
);

create table users (
	id integer not null auto_increment,
	address varchar(255) not null,
	name varchar(255) not null,
	password varchar(255) not null,
	phone varchar(255) not null,
	surname varchar(255) not null,
	email varchar(255) not null,
	primary key (id)
);

alter table users
	add constraint UK_du5v5sr43g5bfnji4vb8hg5s3 unique (phone);

alter table users
	add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (email);

alter table user_roles
	add constraint FKhfh9dx7w3ubf1co1vdev94g3f
	foreign key (user_id)
	references users (id);