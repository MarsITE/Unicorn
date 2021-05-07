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

