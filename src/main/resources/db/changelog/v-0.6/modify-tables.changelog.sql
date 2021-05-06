alter table users
    add if not exists registration_token varchar(255);

drop table if exists users_info_projects;
create table users_info_projects
(
    users_info_projects_id uuid not null
        constraint users_info_projects_pkey
            primary key,
    user_info_id           uuid
        constraint fkhc4ji5kowri8n453fppyyxver
            references users_info,
    project_id             uuid
        constraint fkek070q97cjrc8ujqtt0ss6r48
            references projects,
    is_approve             boolean,
    worker_rating_id       uuid
        constraint fkk896usk8xj0mg21d5r4ysj5aj
            references ratings
);

alter table users_info_projects
    owner to postgres;

alter table projects
    rename column employer_user_id to owner_id;
alter table projects
    add column worker_id uuid
        constraint fkk896usk8xj0mg21d5r4ysj523
            references users_info_projects,
    drop constraint if exists uk65pvcsrn9p0rbe40od9tf824q;

alter table projects
    add constraint name_owner_id unique (name, owner_id);

alter table users_info
    drop column if exists users_info_projects_id,
    add if not exists project_id uuid
        constraint fkk896usk8x12mg21d5r4ysj523
            references users_info_projects;


