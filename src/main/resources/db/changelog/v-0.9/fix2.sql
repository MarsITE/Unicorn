alter table skills
    drop column if exists is_approved;
alter table projects
    drop constraint if exists fkl60dr7cx3pm32he12stq3amdw;