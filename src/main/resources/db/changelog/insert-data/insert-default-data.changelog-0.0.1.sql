-- set default roles
INSERT INTO roles(
    role_id, name_of_role)
VALUES (gen_random_uuid(), 'admin');
INSERT INTO roles(
    role_id, name_of_role)
VALUES (gen_random_uuid(), 'worker');
INSERT INTO roles(
    role_id, name_of_role)
VALUES (gen_random_uuid(), 'employer');



-- set default skills
INSERT INTO public.skills(
    skill_id, enabled, name_of_skill)
VALUES (gen_random_uuid(), true, 'cooking');
INSERT INTO public.skills(
    skill_id, enabled, name_of_skill)
VALUES (gen_random_uuid(), true, 'repair electronic');
INSERT INTO public.skills(
    skill_id, enabled, name_of_skill)
VALUES (gen_random_uuid(), true, 'clean the carpets');
INSERT INTO public.skills(
    skill_id, enabled, name_of_skill)
VALUES (gen_random_uuid(), true, 'washing the cars');
INSERT INTO public.skills(
    skill_id, enabled, name_of_skill)
VALUES (gen_random_uuid(), true, 'painting the walls');
INSERT INTO public.skills(
    skill_id, enabled, name_of_skill)
VALUES (gen_random_uuid(), true, 'repair TV');



-- set first admin
INSERT INTO public.users(
    user_id, account_status, date_of_birth, date_of_creation, email, first_name, image_url, last_name, password, work_status)
VALUES (gen_random_uuid(), true, null, now(), 'admin@gmail.com', null, null, null, '111111', null);