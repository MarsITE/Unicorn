alter table projects
alter column name type varchar(50);

alter table projects
    add constraint projects unique(name, owner_id);

alter table users
add token varchar(255),
alter column email type varchar(50);

