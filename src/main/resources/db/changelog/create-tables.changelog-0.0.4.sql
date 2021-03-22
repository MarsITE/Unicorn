create table if not exists projects
(
    project_id                uuid not null
        constraint projects_pkey
            primary key
        constraint fkl60dr7cx3pm32he12stq3amdw
            references users_info,
    date_of_creation          date,
    description               varchar(2000),
    name                      varchar(20),
    project_status            varchar(255),
    employer_user_id          uuid
        constraint fkocqnwtcu0osp5wbuehxaohr1y
            references users,
    employerrating_raiting_id uuid
        constraint fkc0o2hl0qbw2ndbvaowngumlct
            references ratings,
    worker_user_id            uuid
        constraint fkenorci719xxabc0pv7ub8fkre
            references users,
    workerrating_raiting_id   uuid
        constraint fkceyvnb0c0ilonh0oismkbepyg
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
    raiting_id uuid not null
        constraint ratings_pkey
            primary key,
    comment    varchar(100),
    rate       real
        constraint ratings_rate_check
            check ((rate <= (5)::double precision) AND (rate >= (1)::double precision))
);

alter table ratings
    owner to postgres;

create table if not exists roles
(
    role_id      uuid not null
        constraint roles_pkey
            primary key,
    name_of_role varchar(20)
);

alter table roles
    owner to postgres;

create table if not exists skills
(
    skill_id      uuid not null
        constraint skills_pkey
            primary key,
    name_of_skill varchar(20)
);

alter table skills
    owner to postgres;

create table if not exists users
(
    user_id           uuid not null
        constraint users_pkey
            primary key,
    is_active_account varchar(255),
    date_of_creation  date,
    email             varchar(20),
    password          varchar(255),
    user_info_id           uuid
        constraint fk49cpkihie8ykk50kuq7yh242e
            references users_info
);

alter table users
    owner to postgres;

create table if not exists users_info
(
    user_info_id           uuid not null
        constraint users_info_pkey
            primary key,
    general_rating         real
        constraint users_info_general_rating_check
            check ((general_rating <= (5)::double precision) AND (general_rating >= (1)::double precision)),
    is_show_info           boolean,
    link_to_social_network varchar(255),
    phone                  varchar(15),
    date_of_birth     date,
    first_name        varchar(20),
    image             oid,
    last_name         varchar(20),
    work_status       varchar(255)

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