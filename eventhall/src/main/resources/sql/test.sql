SELECT *
FROM users;

SELECT *
FROM staff;

SELECT *
FROM event_halls;

SELECT *
FROM equipment;

SELECT *
FROM reservations;

SELECT *
FROM equipment_allocations;

SELECT 		r.reservation_id, u.name AS customer_name, h.hall_name, s.name AS staff_name, r.event_date
FROM 		reservations r
JOIN		users u ON r.user_id = u.user_id
JOIN		event_halls h ON r.hall_id = h.hall_id
JOIN		staff s ON r.staff_id = s.staff_id;