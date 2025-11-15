-- Event Hall Reservation System Database Schema
-- CCINFOM-S27-09 | Group 9
-- Members: Ancheta, Bongco, Macatangay, Manalo
-- Date: November 2025

DROP DATABASE IF EXISTS event_hall_reservation;

CREATE DATABASE event_hall_reservation;
USE event_hall_reservation;

-- ===============================
-- USERS TABLE
-- This is the table for the users.
-- ===============================
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    type ENUM('Customer', 'Admin') NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    created_on DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- EVENT HALLS TABLE
-- This is the table for the event halls.
-- ===============================
CREATE TABLE Event_Halls (
    hall_id INT AUTO_INCREMENT PRIMARY KEY,
    hall_name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    location VARCHAR(100),
    status ENUM('Available', 'Booked', 'Under Maintenance') DEFAULT 'Available'
);

-- ===============================
-- STAFF TABLE
-- This is the table for the staff.
-- ===============================
CREATE TABLE Staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(50),
    phone VARCHAR(20)
);

-- ===============================
-- EQUIPMENT TABLE
-- This is the table for the equipments
-- ===============================
CREATE TABLE Equipment (
    equipment_id INT AUTO_INCREMENT PRIMARY KEY,
    equipment_name VARCHAR(100) NOT NULL,
    quantity_total INT NOT NULL,
    status ENUM('Available', 'In Use', 'Maintenance') DEFAULT 'Available'
);

-- ===============================
-- RESERVATIONS TABLE
-- This is the table for the reservations.
-- ===============================
CREATE TABLE Reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    hall_id INT NOT NULL,
    staff_id INT NOT NULL,
    created_on DATETIME DEFAULT CURRENT_TIMESTAMP,
    starts_on DATETIME NOT NULL,
    ends_on DATETIME NOT NULL,
    event_type VARCHAR(100),
    guest_count INT,
    status ENUM('Pending', 'Confirmed', 'Canceled', 'Completed') DEFAULT 'Pending',
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (hall_id) REFERENCES Event_Halls(hall_id),
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id)
);

-- ===============================
-- EQUIPMENT ALLOCATIONS TABLE
-- This is the table for the equipment allocations.
-- ===============================
CREATE TABLE Equipment_Allocations (
    allocation_id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_id INT NOT NULL,
    equipment_id INT NOT NULL,
    quantity_used INT NOT NULL,
    FOREIGN KEY (reservation_id) REFERENCES Reservations(reservation_id)
        ON DELETE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES Equipment(equipment_id)
        ON DELETE CASCADE
);

-- ===============================
-- SAMPLE DATA INSERTION 
-- Can be updated and manipulates as project develops
-- ===============================

INSERT INTO Users (type, name, email, phone, password)
VALUES
('Admin', 'admin', 'admin@email.com', '09981234567', 'password'),
('Customer', 'Juan Dela Cruz', 'juan@email.com', '09171234567', 'password');

INSERT INTO Event_Halls (hall_name, capacity, location, status)
VALUES
('Emerald Hall', 200, '2nd Floor, Building A', 'Available'),
('Ruby Hall', 100, '3rd Floor, Building B', 'Available');

INSERT INTO Equipment (equipment_name, quantity_total)
VALUES
('Projector', 10),
('Sound System', 5),
('Table', 50),
('Chair', 200);