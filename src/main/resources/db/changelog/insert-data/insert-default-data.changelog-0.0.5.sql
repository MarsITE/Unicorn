-- set default roles
INSERT INTO roles(
    role_id, name)
VALUES ('3d75b56c-b775-4705-a6cf-080ea743b121', 'ADMIN');
INSERT INTO roles(
    role_id, name)
VALUES (gen_random_uuid(), 'WORKER');
INSERT INTO roles(
    role_id, name)
VALUES (gen_random_uuid(), 'EMPLOYER');

INSERT INTO public.users_info (user_info_id, general_rating)
values ('4debc947-c1af-4f6e-9aab-1e79e23672e6', 5);

INSERT INTO public.users (user_id, account_status, creation_date, email, password, user_info_id)
VALUES ('f6cea10a-2f9d-4feb-82ba-b600bb4cb5f0', 'ACTIVE', now(), 'admin@gmail.com', '111111', '4debc947-c1af-4f6e-9aab-1e79e23672e6');

INSERT INTO public.users_roles (user_id, role_id) VALUES ('f6cea10a-2f9d-4feb-82ba-b600bb4cb5f0', '3d75b56c-b775-4705-a6cf-080ea743b121');

