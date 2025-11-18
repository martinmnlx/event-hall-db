-- Monthly Reservation Report per Hall
SELECT h.hall_name, COUNT(r.reservation_id) as reservation_count 
FROM Reservations r 
JOIN Event_Halls h ON r.hall_id = h.hall_id 
WHERE MONTH(r.event_date) = 11 AND YEAR(r.event_date) = 2025
AND r.status = Confirmed
GROUP BY h.hall_name;

-- Hall Utilization Report
SELECT h.hall_name, COUNT(DISTINCT r.event_date) as days_booked 
FROM Reservations r 
JOIN Event_Halls h ON r.hall_id = h.hall_id 
WHERE r.event_date BETWEEN '2025-01-01' AND '2025-06-01' 
AND r.status = 'Confirmed' 
GROUP BY h.hall_name;

-- Equipment Utilization Report
SELECT e.equipment_name, COUNT(ea.allocation_id) as reservation_count, SUM(ea.quantity_used) as quantity_rented 
FROM equipment_allocations ea 
JOIN reservations r ON ea.reservation_id = r.reservation_id 
JOIN equipment e ON ea.equipment_id = e.equipment_id 
WHERE r.event_date BETWEEN '2025-01-01' AND '2025-06-01' 
AND r.status = 'Confirmed' 
GROUP BY e.equipment_name;

-- Event Type Report
SELECT event_type, COUNT(*) as event_type_count 
FROM Reservations 
WHERE status = 'Confirmed' 
GROUP BY event_type;