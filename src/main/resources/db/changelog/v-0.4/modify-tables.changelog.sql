create unique index skills_name_id on skills (name);
create unique index roles_name_id on roles (name);
create unique index users_email_id on users (email);

alter table projects
    alter column creation_date type timestamp,
    alter column description type varchar(2000);

alter table skills
    add column enabled boolean;

alter table users
    rename column is_active_account to account_status;

alter table users
    add constraint email_unique_id unique using index users_email_id;

alter table skills
    add constraint skill_unique_id unique using index skills_name_id;

alter table roles
    add constraint role_unique_id unique using index roles_name_id;
