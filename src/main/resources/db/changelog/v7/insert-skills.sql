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