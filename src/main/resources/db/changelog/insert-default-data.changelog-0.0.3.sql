-- set default roles
INSERT INTO roles(
    role_id, name)
VALUES (gen_random_uuid(), 'ADMIN');
INSERT INTO roles(
    role_id, name)
VALUES (gen_random_uuid(), 'WORKER');
INSERT INTO roles(
    role_id, name)
VALUES (gen_random_uuid(), 'EMPLOYER');



-- set default skills
INSERT INTO public.skills(
    skill_id, enabled, name)
VALUES (gen_random_uuid(), true, 'cooking');
INSERT INTO public.skills(
    skill_id, enabled, name)
VALUES (gen_random_uuid(), true, 'repair electronic');
INSERT INTO public.skills(
    skill_id, enabled, name)
VALUES (gen_random_uuid(), true, 'clean the carpets');
INSERT INTO public.skills(
    skill_id, enabled, name)
VALUES (gen_random_uuid(), true, 'washing the cars');
INSERT INTO public.skills(
    skill_id, enabled, name)
VALUES (gen_random_uuid(), true, 'painting the walls');
INSERT INTO public.skills(
    skill_id, enabled, name)
VALUES (gen_random_uuid(), true, 'repair TV');



-- set first admin
INSERT INTO public.users(
    user_id, account_status, creation_date, email, password)
VALUES (gen_random_uuid(), 'ACTIVE',  now(), 'admin@gmail.com',  '111111');