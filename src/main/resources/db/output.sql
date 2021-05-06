-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/db.changelog-master.xml
-- Ran at: 06.05.21, 14:36
-- Against: postgres@jdbc:postgresql://localhost:5432/work_db
-- Liquibase version: 3.10.3
-- *********************************************************************

SET SEARCH_PATH TO public;

SET SEARCH_PATH TO public;

-- Create Database Lock Table
CREATE TABLE databasechangeloglock (ID INTEGER NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITHOUT TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT DATABASECHANGELOGLOCK_PKEY PRIMARY KEY (ID));

-- Initialize Database Lock Table
DELETE FROM databasechangeloglock;

INSERT INTO databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
UPDATE databasechangeloglock SET LOCKED = TRUE, LOCKEDBY = 'DESKTOP-0AFEH81 (192.168.88.250)', LOCKGRANTED = '2021-05-06 14:36:47.659' WHERE ID = 1 AND LOCKED = FALSE;

SET SEARCH_PATH TO public;

-- Create Database Change Log Table
CREATE TABLE databasechangelog (ID VARCHAR(255) NOT NULL, AUTHOR VARCHAR(255) NOT NULL, FILENAME VARCHAR(255) NOT NULL, DATEEXECUTED TIMESTAMP WITHOUT TIME ZONE NOT NULL, ORDEREXECUTED INTEGER NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONTEXTS VARCHAR(255), LABELS VARCHAR(255), DEPLOYMENT_ID VARCHAR(10));

SET SEARCH_PATH TO public;

SET SEARCH_PATH TO public;

-- Changeset C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.1/create-tables.changelog.sql::raw::includeAll
SET SEARCH_PATH TO public;

create table users_info
(
    user_info_id           uuid not null
        constraint users_info_pkey
            primary key,
    general_rating         real
        constraint users_info_general_rating_check
            check ((general_rating <= (5)::double precision) AND (general_rating >= (1)::double precision)),
    is_show_info           boolean,
    link_to_social_network varchar(255),
    phone                  varchar(15)
);
alter table users_info
    owner to postgres;

create table users
(
    user_id           uuid not null
        constraint users_pkey
            primary key,
    is_active_account varchar(255),
    date_of_birth     date,
    date_of_creation  date,
    email             varchar(20),
    first_name        varchar(20),
    image             oid,
    last_name         varchar(20),
    password          varchar(255),
    work_status       varchar(255),
    user_user_id           uuid
        constraint fk49cpkihie8ykk50kuq7yh242e
            references users_info
);
alter table users
    owner to postgres;

create table ratings
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
create table roles
(
    role_id      uuid not null
        constraint roles_pkey
            primary key,
    name_of_role varchar(20)
);
alter table roles
    owner to postgres;
create table skills
(
    skill_id      uuid not null
        constraint skills_pkey
            primary key,
    name_of_skill varchar(20)
);
alter table skills
    owner to postgres;

create table users_info_skills
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
create table users_roles
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

create table projects
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
    employer_user_id          uuid not null
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

create table projects_skills
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

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('raw', 'includeAll', 'C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.1/create-tables.changelog.sql', NOW(), 1, '8:e8ea90f47a8f8e2f8b61e88a70c69e18', 'sql', '', 'EXECUTED', NULL, NULL, '3.10.3', '0301008005');

-- Changeset C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.2/modify-tables.changelog.sql::raw::includeAll
SET SEARCH_PATH TO public;

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

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('raw', 'includeAll', 'C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.2/modify-tables.changelog.sql', NOW(), 2, '8:e47cff843e99f26535641e6feafda520', 'sql', '', 'EXECUTED', NULL, NULL, '3.10.3', '0301008005');

-- Changeset C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.3/modify-tables.changelog.sql::raw::includeAll
SET SEARCH_PATH TO public;

alter table skills
    rename column name_of_skill to name;

alter table roles
    rename column name_of_role to name;

alter table projects
    rename column date_of_creation to creation_date;

alter table projects
    rename column employerrating_raiting_id to employer_rating_rating_id;
alter table projects
    rename column workerrating_raiting_id to worker_rating_rating_id;

alter table ratings
    rename column raiting_id to rating_id;

alter table users
    rename column date_of_creation to creation_date;

alter table users_info
    rename column date_of_birth to birth_date;

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('raw', 'includeAll', 'C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.3/modify-tables.changelog.sql', NOW(), 3, '8:cdcc4a9a4a67e20e02be99b21ff1746c', 'sql', '', 'EXECUTED', NULL, NULL, '3.10.3', '0301008005');

-- Changeset C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.4/modify-tables.changelog.sql::raw::includeAll
SET SEARCH_PATH TO public;

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

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('raw', 'includeAll', 'C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.4/modify-tables.changelog.sql', NOW(), 4, '8:4e40d6b045935411b4f2154be424ebeb', 'sql', '', 'EXECUTED', NULL, NULL, '3.10.3', '0301008005');

-- Changeset C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.5/modify-tables.changelog.sql::raw::includeAll
SET SEARCH_PATH TO public;

alter table projects
alter column name type varchar(50);

alter table users
add if not exists refresh_token varchar(255),
alter column email type varchar(50);

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('raw', 'includeAll', 'C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.5/modify-tables.changelog.sql', NOW(), 5, '8:be876a67160c1c092f1081b4d96274b5', 'sql', '', 'EXECUTED', NULL, NULL, '3.10.3', '0301008005');

-- Changeset C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.6/modify-tables.changelog.sql::raw::includeAll
SET SEARCH_PATH TO public;

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

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('raw', 'includeAll', 'C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.6/modify-tables.changelog.sql', NOW(), 6, '8:dcc410e49af6cc136f39b5af661ef73f', 'sql', '', 'EXECUTED', NULL, NULL, '3.10.3', '0301008005');

-- Changeset C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.7/modify-tables.changelog.sql::raw::includeAll
SET SEARCH_PATH TO public;

alter table users
    rename column  user_user_id to user_info_id;
alter table users
    drop column refresh_token;

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('raw', 'includeAll', 'C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.7/modify-tables.changelog.sql', NOW(), 7, '8:cfe9ddf54672aa5642f2c8e3643c82be', 'sql', '', 'EXECUTED', NULL, NULL, '3.10.3', '0301008005');

-- Changeset C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.8/modify-tables.changelog.sql::raw::includeAll
SET SEARCH_PATH TO public;

alter table users_info
    add column description varchar(2000);
alter table users_info
    rename column image to image_url;
alter table users_info
    alter column image_url type varchar(255);
alter table projects
    rename column employer_rating_rating_id to owner_rating_id;
alter table skills
    add column is_approved boolean;
alter table projects
    drop column worker_user_id,
    drop column worker_rating_rating_id;

INSERT INTO databasechangelog (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('raw', 'includeAll', 'C:/Users/javdi/Desktop/pr/unicorn/src/main/resources/db/changelog/v-0.8/modify-tables.changelog.sql', NOW(), 8, '8:d86b64ee167bdb0b65e5893ab006de30', 'sql', '', 'EXECUTED', NULL, NULL, '3.10.3', '0301008005');

-- Release Database Lock
SET SEARCH_PATH TO public;

UPDATE databasechangeloglock SET LOCKED = FALSE, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1;

SET SEARCH_PATH TO public;

