alter table users
    rename column  user_user_id to user_info_id;
alter table users
    drop column refresh_token;
