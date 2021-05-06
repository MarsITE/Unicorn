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