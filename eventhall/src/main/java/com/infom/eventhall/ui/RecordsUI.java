package com.infom.eventhall.ui;

import com.infom.eventhall.model.*;
import com.infom.eventhall.service.*;
import com.infom.eventhall.ui.forms.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RecordsUI extends JPanel {

    private final AppFrame app;

    private final UserService userService;
    private final StaffService staffService;
    private final EventHallService eventHallService;
    private final EquipmentService equipmentService;
    private final ReservationService reservationService;
    private final EquipmentAllocationService equipmentAllocationService;

    private final DefaultTableModel model;
    private final JTable table;

    private final JLabel mainLabel;
    private final JLabel recordsLabel;
    private final JLabel rowsLabel;
    private final JComboBox<String> recordsDropdown;

    private final JButton createButton;
    private final JButton updateButton;
    private final JButton deleteButton;
    private final JButton logoutButton;

    private String currentRecord;

    public RecordsUI(AppFrame app, UserService u, StaffService s, EventHallService h, EquipmentService q, ReservationService r, EquipmentAllocationService a) {
        this.app = app;
        this.userService = u;
        this.staffService = s;
        this.eventHallService = h;
        this.equipmentService = q;
        this.reservationService = r;
        this.equipmentAllocationService = a;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        mainLabel = app.createLabel("View Records Tables", Color.BLUE, 60f, 3);

        recordsLabel = app.createLabel("Record: ", Color.BLACK, 20f, 2);
        recordsDropdown = new JComboBox<>(new String[]{
                "Reservations", "Users", "Staff", "Event Halls", "Equipment", "Equipment Allocations"
        });
        recordsDropdown.setFont(app.getRegularFont().deriveFont(16f));
        recordsDropdown.setBackground(Color.WHITE);
        recordsDropdown.setMaximumSize(new Dimension(200, 60));
        ((JLabel) recordsDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        recordsDropdown.addActionListener(e -> {
            currentRecord = (String) recordsDropdown.getSelectedItem();
            refresh();
        });
        currentRecord = (String) recordsDropdown.getSelectedItem();

        rowsLabel = app.createLabel("0 Rows", Color.BLUE, 20f, 3);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.add(recordsLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(8, 0)));
        filterPanel.add(recordsDropdown);
        filterPanel.add(Box.createRigidArea(new Dimension(24, 0)));
        filterPanel.add(rowsLabel);
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(40);
        table.getTableHeader().setFont(app.getBoldFont().deriveFont(16f));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setFont(app.getRegularFont().deriveFont(16f));

        DefaultTableCellRenderer paddingRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return c;
            }
        };

        table.setDefaultRenderer(Object.class, paddingRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        // Buttons
        createButton = app.createButton("Create", Color.BLUE, 20f, false);
        updateButton = app.createButton("Update", Color.BLUE, 20f, false);
        deleteButton = app.createButton("Delete", Color.BLUE, 20f, false);
        logoutButton = app.createButton("Return to Dashboard", Color.BLUE, 20f, false);

        // Assign single listeners
        createButton.addActionListener(e -> openCreateForm());
        updateButton.addActionListener(e -> openUpdateForm());
        deleteButton.addActionListener(e -> deleteSelectedRecord());
        logoutButton.addActionListener(e -> app.showScreen("admin"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(createButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(updateButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(820, 0)));
        buttonPanel.add(logoutButton);

        add(Box.createVerticalStrut(40));
        add(mainLabel);
        add(Box.createVerticalStrut(60));
        add(filterPanel);
        add(Box.createVerticalStrut(40));
        add(scrollPane);
        add(Box.createVerticalStrut(40));
        add(buttonPanel);
        add(Box.createVerticalStrut(40));

        refresh();
    }

    private void openCreateForm() {
        switch (currentRecord) {
            case "Users" -> new UserForm(app, userService, null).setVisible(true);
            case "Staff" -> new StaffForm(app, staffService, null).setVisible(true);
            case "Event Halls" -> new EventHallForm(app, eventHallService, null).setVisible(true);
            case "Equipment" -> new EquipmentForm(app, equipmentService, null).setVisible(true);
            case "Reservations" -> new ReservationForm(app, reservationService, null, eventHallService, userService, staffService).setVisible(true);
            case "Equipment Allocations" -> new EquipmentAllocationForm(app, equipmentAllocationService, null, reservationService, equipmentService, eventHallService).setVisible(true);
        }
        refresh();
    }

    private void openUpdateForm() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        switch (currentRecord) {
            case "Users" -> {
                int userId = (int) model.getValueAt(row, 0);
                new UserForm(app, userService, userService.getUserById(userId)).setVisible(true);
            }
            case "Staff" -> {
                int staffId = (int) model.getValueAt(row, 0);
                new StaffForm(app, staffService, staffService.getStaffById(staffId)).setVisible(true);
            }
            case "Event Halls" -> {
                int hallId = (int) model.getValueAt(row, 0);
                new EventHallForm(app, eventHallService, eventHallService.getHallById(hallId)).setVisible(true);
            }
            case "Equipment" -> {
                int eqId = (int) model.getValueAt(row, 0);
                new EquipmentForm(app, equipmentService, equipmentService.getEquipmentById(eqId)).setVisible(true);
            }
            case "Reservations" -> {
                int resId = (int) model.getValueAt(row, 0);
                new ReservationForm(app, reservationService, reservationService.getReservationById(resId), eventHallService, userService, staffService).setVisible(true);
            }
            case "Equipment Allocations" -> {
                int allocId = (int) model.getValueAt(row, 0);
                new EquipmentAllocationForm(app, equipmentAllocationService, equipmentAllocationService.getAllocationById(allocId), reservationService, equipmentService, eventHallService).setVisible(true);
            }
        }
        refresh();
    }

    private void deleteSelectedRecord() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this " + currentRecord + " record?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            switch (currentRecord) {
                case "Users" -> userService.deleteUser((int) model.getValueAt(row, 0));
                case "Staff" -> staffService.deleteStaff((int) model.getValueAt(row, 0));
                case "Event Halls" -> eventHallService.deleteEventHall((int) model.getValueAt(row, 0));
                case "Equipment" -> equipmentService.deleteEquipment((int) model.getValueAt(row, 0));
                case "Reservations" -> reservationService.deleteReservation((int) model.getValueAt(row, 0));
            }
            refresh();
            JOptionPane.showMessageDialog(this, "Deleted successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // -------------------- REFRESH TABLE --------------------
    public void refresh() {
        switch (currentRecord) {
            case "Users" -> showUsersTable();
            case "Staff" -> showStaffTable();
            case "Event Halls" -> showEventHallsTable();
            case "Equipment" -> showEquipmentTable();
            case "Reservations" -> showReservationsTable();
            case "Equipment Allocations" -> showEquipmentAllocationsTable();
        }

        rowsLabel.setText(String.valueOf(table.getRowCount()) + " Rows");
    }

    // -------------------- TABLE DATA --------------------
    private void showUsersTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
        model.addColumn("User ID");
        model.addColumn("Type");
        model.addColumn("Name");
        model.addColumn("Email");
        model.addColumn("Phone Number");
        model.addColumn("Password");
        model.addColumn("Created On");

        List<User> users = userService.getAllUsers();
        for (User user : users) {
            model.addRow(new Object[]{
                    user.getUserId(),
                    user.getType(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getPassword(),
                    user.getCreatedOn()
            });
        }
    }

    private void showStaffTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
        model.addColumn("Staff ID");
        model.addColumn("Name");
        model.addColumn("Phone Number");
        model.addColumn("Role");

        List<Staff> staffs = staffService.getAllStaff();
        for (Staff staff : staffs) {
            model.addRow(new Object[]{
                    staff.getStaffId(),
                    staff.getName(),
                    staff.getPhoneNumber(),
                    staff.getRole()
            });
        }
    }

    private void showEventHallsTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
        model.addColumn("Hall ID");
        model.addColumn("Name");
        model.addColumn("Capacity");
        model.addColumn("Location");
        model.addColumn("Status");

        List<EventHall> halls = eventHallService.getAllEventHalls();
        for (EventHall hall : halls) {
            model.addRow(new Object[]{
                    hall.getHallId(),
                    hall.getHallName(),
                    hall.getCapacity(),
                    hall.getLocation(),
                    hall.getStatus()
            });
        }

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                if (value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "Available" -> c.setForeground(Color.decode("#5BB450"));
                        case "Maintenance" -> c.setForeground(Color.decode("#F94449"));
                        default -> c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
    }

    private void showEquipmentTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
        model.addColumn("Equipment ID");
        model.addColumn("Name");
        model.addColumn("Total Quantity");
        model.addColumn("Status");

        List<Equipment> equipments = equipmentService.getAllEquipments();
        for (Equipment eq : equipments) {
            model.addRow(new Object[]{
                    eq.getEquipmentId(),
                    eq.getEquipmentName(),
                    eq.getQuantityTotal(),
                    eq.getEquipment()
            });
        }

        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                if (value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "InUse" -> c.setForeground(Color.ORANGE);
                        case "Available" -> c.setForeground(Color.decode("#5BB450"));
                        case "Maintenance" -> c.setForeground(Color.decode("#F94449"));
                        default -> c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
    }

    private void showReservationsTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
        model.addColumn("Reservation ID");
        model.addColumn("Event Hall");
        model.addColumn("Customer");
        model.addColumn("Email");
        model.addColumn("Staff In-Charge");
        model.addColumn("Date Reserved");
        model.addColumn("Event Type");
        model.addColumn("Guest Count");
        model.addColumn("Date Created");
        model.addColumn("Status");

        List<Reservation> reservations = reservationService.getAllReservations();
        for (Reservation r : reservations) {
            Staff staff = staffService.getStaffById(r.getStaffId());
            String staffName = (staff != null ? staff.getName() : "");

            model.addRow(new Object[]{
                    r.getReservationId(),
                    eventHallService.getHallById(r.getHallId()).getHallName(),
                    userService.getUserById(r.getUserId()).getName(),
                    userService.getUserById(r.getUserId()).getEmail(),
                    staffName,
                    r.getEventDate(),
                    r.getEventType(),
                    r.getGuestCount(),
                    r.getCreatedOn(),
                    r.getStatus()
            });
        }

        table.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                if (value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "Pending" -> c.setForeground(Color.ORANGE);
                        case "Confirmed" -> c.setForeground(Color.decode("#5BB450"));
                        case "Canceled" -> c.setForeground(Color.decode("#F94449"));
                        case "Completed" -> c.setForeground(Color.GRAY);
                        default -> c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
    }

    private void showEquipmentAllocationsTable() {
        model.setRowCount(0);
        model.setColumnCount(0);
        model.addColumn("Allocation ID");
        model.addColumn("Reservation ID");
        model.addColumn("Event Hall");
        model.addColumn("Use Date");
        model.addColumn("Equipment");
        model.addColumn("Quantity");

        List<EquipmentAllocation> allocs = equipmentAllocationService.getAllAllocations();
        for (EquipmentAllocation ea : allocs) {
            model.addRow(new Object[]{
                    ea.getAllocationId(),
                    ea.getReservationId(),
                    eventHallService.getHallById(reservationService.getReservationById(ea.getReservationId()).getHallId()).getHallName(),
                    reservationService.getReservationById(ea.getReservationId()).getEventDate(),
                    equipmentService.getEquipmentById(ea.getEquipmentId()).getEquipmentName(),
                    ea.getQuantityUsed()
            });
        }
    }
}
