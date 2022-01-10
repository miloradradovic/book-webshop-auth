insert into users (address, name, password, phone, surname, email) values ("address1", "Milorad",
"$2y$10$Gce/AEiSA4gNRe6280j4J.TBplRJefFpcrvDTicr7TduP/MTx.Es6", "phone1", "Radovic", "email1@email.com");
insert into users (address, name, password, phone, surname, email) values ("admin", "Admin",
"$2y$10$Gce/AEiSA4gNRe6280j4J.TBplRJefFpcrvDTicr7TduP/MTx.Es6", "admin", "Admin", "admin@admin.com");

insert into user_roles (user_id, roles) values (1, "ROLE_USER");
insert into user_roles (user_id, roles) values (2, "ROLE_ADMIN");