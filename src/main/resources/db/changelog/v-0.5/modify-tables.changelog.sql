alter table projects
alter column name type varchar(50);

alter table users
add if not exists refresh_token varchar(255),
alter column email type varchar(50);


