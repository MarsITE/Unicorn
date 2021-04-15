 
create table if not exists projects
(
    project_id       uuid not null
        constraint projects_pkey
            primary key,
    creation_date    timestamp,
    description      varchar(2000),
    name             varchar(20),
    project_status   varchar(255),
    owner_id         uuid not null
        constraint fkmueqy6cpcwpfl8gnnag4idjt9
            references users,
    owner_rating_id  uuid
        constraint fk6m9gn8ko2tjjw4b985xwp41o2
            references ratings,
    user_info_id     uuid
        constraint fkj8qkkmwp3ij2hwxrv4ivpc0d5
            references users_info,
    worker_id        uuid
        constraint fkivvo1tyowrboqy406njlkn3ac
            references users,
    worker_rating_id uuid
        constraint fkk896usk8xj0mg21d5r4ysj5aj
            references ratings
);

alter table projects
    owner to postgres;

 
create table if not exists projects_skills
(
    project_id uuid not null
        constraint fkicynes8ssd4mqwy1vjagjr3ar
            references projects,
    skill_id   uuid not null
        constraint fk9d7l7u8twaseq9afq5ij61uoc
            references skills,
    constraint projects_skills_pkey
        primary key (project_id, skill_id)
);

alter table projects_skills
    owner to postgres;

 
create table if not exists ratings
(
    rating_id uuid not null
        constraint ratings_pkey
            primary key,
    comment   varchar(100),
    rate      real
        constraint ratings_rate_check
            check ((rate <= (5)::double precision) AND (rate >= (1)::double precision))
);

alter table ratings
    owner to postgres;

 
create table if not exists roles
(
    role_id uuid not null
        constraint roles_pkey
            primary key,
    name    varchar(20)
);

alter table roles
    owner to postgres;

 
create table if not exists skills
(
    skill_id uuid not null
        constraint skills_pkey
            primary key,
    enabled  boolean,
    name     varchar(20)
);

alter table skills
    owner to postgres;

 
create table if not exists users
(
    user_id        uuid         not null
        constraint users_pkey
            primary key,
    account_status varchar(255),
    creation_date  timestamp,
    email          varchar(20)  not null
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    password       varchar(255) not null,
    user_info_id   uuid
        constraint fka4pav0806byoi689xgfobjqic
            references users_info
);

alter table users
    owner to postgres;

 
create table if not exists users_info
(
    user_info_id           uuid not null
        constraint users_info_pkey
            primary key,
    birth_date             date,
    description            varchar(2000),
    first_name             varchar(20),
    general_rating         double precision default 0
        constraint users_info_general_rating_check
            check ((general_rating <= (5)::double precision) AND (general_rating >= (1)::double precision)),
    image_url              varchar(255),
    is_show_info           boolean          default true,
    last_name              varchar(20),
    link_to_social_network varchar(255),
    phone                  varchar(15),
    work_status            varchar(255)
);

alter table users_info
    owner to postgres;

 
create table if not exists users_info_skills
(
    user_info_id uuid not null
        constraint fkjc8r591tcdncsi70etdrs0otx
            references users_info,
    skill_id     uuid not null
        constraint fkafl29nybju2b4umrhm2v8km4p
            references skills,
    constraint users_info_skills_pkey
        primary key (user_info_id, skill_id)
);

alter table users_info_skills
    owner to postgres;

 
create table if not exists users_roles
(
    user_id uuid not null
        constraint fk2o0jvgh89lemvvo17cbqvdxaa
            references users,
    role_id uuid not null
        constraint fkj6m8fwv7oqv74fcehir1a9ffy
            references roles,
    constraint users_roles_pkey
        primary key (user_id, role_id)
);

alter table users_roles
    owner to postgres;

