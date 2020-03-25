INSERT INTO user_details(
	createdAt, email, firstname,  lastname, password, phone, role, username, status, resetRequested)
	VALUES (now(), 'nasuser@superuser', 'Super', 'User', '$2a$10$087rXhlXWdJCpgcbej3FdeAJ.kPOdydd7QqeKtMox2oL0JeeS4Ury', '', 'ROLE_ADMIN', 'superuser',true,true) ON CONFLICT (username)
DO NOTHING;

