alter table users
    drop column first_name,
    drop column image,
    drop column last_name,
    drop column work_status,
    drop column date_of_birth;

alter table users_info
    add column date_of_birth date,
    add column first_name varchar(20),
    add column image oid,
    add column last_name varchar(20),
    add column work_status varchar(20);
