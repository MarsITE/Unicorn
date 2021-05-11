ALTER TABLE users_info_projects
    ADD CONSTRAINT user_info_project_uq UNIQUE(user_info_id, project_id);