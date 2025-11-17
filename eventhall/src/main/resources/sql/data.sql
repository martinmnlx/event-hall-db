INSERT INTO Users (type, name, email, phone, password)
VALUES
('Admin', 'admin', 'admin@email.com', '09981234567', 'password'),
('Customer', 'Martin', 'martin@email.com', '09214086188', 'password'),
('Customer', 'Sam', 'sam@email.com', '09563824928', 'password'),
('Customer', 'Nina', 'nina@email.com', '09882394752', 'password'),
('Customer', 'Miko', 'miko@email.com', '09245368823', 'password');

INSERT INTO Staff (name, phone, role) 
VALUES
('Juan Dela Cruz', '09171234567', 'Head Staff'),
('Anna Santos', '09987654321', 'Head Staff'),
('Mark Reyes', '09182345678', 'Head Staff'),
('Leah Cruz', '09211234567', 'Head Staff'),
('Carlos Ramos', '09179876543', 'Head Staff'),
('Maria Lopez', '09221234567', 'Head Staff'),
('Rico Mendoza', '09173456789', 'Head Staff'),
('Clara Villanueva', '09981234567', 'Head Staff'),
('Josefa Torres', '09234567890', 'Head Staff'),
('Edgar Lim', '09187654321', 'Head Staff'),
('Luis Fernandez', '09190011223', 'Head Staff'),
('Patricia Gomez', '09223344556', 'Head Staff'),
('Miguel Santos', '09191122334', 'Head Staff'),
('Veronica Cruz', '09224455667', 'Head Staff'),
('Daniel Reyes', '09192233445', 'Head Staff'),
('Monica Villanueva', '09225566778', 'Head Staff'),
('Hector Lopez', '09193344556', 'Head Staff'),
('Carla Mendoza', '09226677889', 'Head Staff'),
('Emilio Torres', '09194455667', 'Head Staff'),
('Isabel Lim', '09227788990', 'Head Staff');

INSERT INTO Event_Halls (hall_name, capacity, location, status)
VALUES
('Azure Grand Hall', 250, 'Pasig City', 'Available'),
('Crystal Event Center', 300, 'Taguig City', 'Available'),
('Emerald Function Hall', 100, 'Mandaluyong City', 'Maintenance'),
('Golden Gate Hall', 180, 'Taguig City', 'Available'),
('Grand Vista Hall', 200, 'Makati City', 'Available'),
('Opal Grand Hall', 210, 'Quezon City', 'Available'),
('Topaz Event Center', 230, 'Makati City', 'Available'),
('Onyx Function Hall', 120, 'Pasig City', 'Available'),
('Royal Orchid Hall', 200, 'Mandaluyong City', 'Available'),
('Sapphire Banquet Hall', 220, 'Pasig City', 'Maintenance'),
('Sunshine Ballroom', 150, 'Quezon City', 'Available'),
('Amethyst Pavilion', 130, 'Makati City', 'Available'),
('Diamond Atrium', 280, 'Pasig City', 'Available'),
('Pearl Heritage Hall', 160, 'Taguig City', 'Maintenance'),
('Ruby Celebration Hall', 190, 'Quezon City', 'Available');

INSERT INTO Equipment (equipment_name, quantity_total)
VALUES
('Fog Machine', 10),
('Laser Lights', 10),
('Karaoke', 5),
('Photo Booth', 5);