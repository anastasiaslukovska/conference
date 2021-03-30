INSERT INTO roles (name) VALUES ('LISTENER');
INSERT INTO roles (name) VALUES ('PRESENTER');
INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO user (name, login, password, enabled) VALUES ('admin', 'admin','$2a$10$bN7OWEvi6rTqJEYbZfDOg.FHmG.xPTDxJR1k9LzsR4O6Nt8zuIKwq', '1');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 3);
INSERT INTO user (name, login, password, enabled) VALUES ('test', 'test','$2a$10$bN7OWEvi6rTqJEYbZfDOg.FHmG.xPTDxJR1k9LzsR4O6Nt8zuIKwq', '1');
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 3);