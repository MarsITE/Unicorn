alter table users
    drop column if exists token;
alter table projects
    drop column if exists user_info_id,
    drop column if exists worker_rating_id;

-- alter table projects add constraint name_owner_id unique (name, owner_id);

drop table if exists users_info_projects;
create table users_info_projects
(
    user_info_project_id uuid not null
        constraint users_info_projects_pkey
            primary key,
    user_info_id          uuid not null
        constraint fkhc4ji5kowri8n453fppyyxver
            references users_info,
    project_id            uuid not null
        constraint fkek070q97cjrc8ujqtt0ss6r48
            references projects,
    is_approve            boolean,
    worker_rating_id      uuid
        constraint fkk896usk8xj0mg21d5r4ysj5aj
            references ratings
);

alter table users_info_projects
    owner to postgres;

