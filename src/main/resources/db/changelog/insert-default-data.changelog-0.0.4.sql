INSERT INTO public.users_info (user_info_id, general_rating)
values ('4debc947-c1af-4f6e-9aab-1e79e23672e6', 5);

INSERT INTO public.users (
    user_id, account_status, date_of_creation, email, password, user_info_id)
VALUES (gen_random_uuid(), 'ACTIVE', now(), 'admin@gmail.com', '111111', '4debc947-c1af-4f6e-9aab-1e79e23672e6');