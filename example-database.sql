INSERT INTO tutor (username, password_hash, first_name, last_name) VALUES
    ('robkoub', '58a886dd0baaed952b1e9365313136fabf54a27792e4040f705623b288da3ee044f32327f77eb7b08d467e6c89a86a3e93e91607daf21b903061e516a74d9ca04f33a3d466ba701c081dc8869380315d', 'Robert', 'Koubek'),
    ('robpatt', 'df38692f0520bc1b6cd73f379b076badcae58a94937561b58630d9ce2c3046c867d7e69039527c7c6ddd63464dfd06fb3a3b53ff82accadffdb12e1a86411216ce1f1d46d6c6137fe31ca5b2390f7aca', 'Robert', 'Pattinson'),
    ('pavmaty', 'c15057bc83d2a107ae514e72e34f948321f9ddd8205eeaec84700411b64342618398fc769203f26051e2ca1cfb14d3b20fcbd8cc46f3600ba1b0adae73f904dcc5e971919093c9b6799753b59cce71f2', 'Pavel', 'Matys'),
    ('zuzvili', '43923870af1294d8e9d5379355dfaa59691cd4d1c79ede044ced8b0849bed0e041eb62c6023410364285f89f9f62f23780f9567ef887c5c5b84252dc042b79063e0e788a6a8c4344f097094a3c6b3904', 'Zuzana', 'Vilímová');

INSERT INTO instrument (instrument, min_level, max_level, tutor_id) VALUES
    ('piano', 1, 10, 1),
    ('trumpet', 1, 6, 2),
    ('oboe', 4, 10, 2),
    ('guitar', 1, 6, 3),
    ('drums', 4, 10, 3),
    ('bass', 1, 6, 4),
    ('guitar', 4, 10, 4);

INSERT INTO tutor_unavailability (end_time, start_time, tutor_id) VALUES
    ('2026-11-23 17:30:00', '2026-11-23 16:00:00', 1),
    ('2026-11-24 19:00:00', '2026-11-24 17:00:00', 2),
    ('2026-11-25 20:00:00', '2026-11-25 18:00:00', 3),
    ('2026-11-26 18:00:00', '2026-11-26 16:30:00', 4),
    ('2026-11-28 12:00:00', '2026-11-28 09:00:00', 1),
    ('2026-11-30 18:30:00', '2026-11-30 16:00:00', 2),
    ('2026-12-01 19:30:00', '2026-12-01 17:30:00', 3),
    ('2026-12-02 17:00:00', '2026-12-02 16:00:00', 4),
    ('2026-12-04 20:00:00', '2026-12-04 18:00:00', 1),
    ('2026-12-05 14:00:00', '2026-12-05 10:00:00', 3);
