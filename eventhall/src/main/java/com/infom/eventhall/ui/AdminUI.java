package com.infom.eventhall.ui;

import com.infom.eventhall.model.*;
import com.infom.eventhall.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class AdminUI extends JPanel {

    private AppFrame app;

    private final UserService userService;
    private final StaffService staffService;
    private final EventHallService eventHallService;
    private final EquipmentService equipmentService;
    private final ReservationService reservationService;

    private final DefaultTableModel model;

    private final JLabel mainLabel;
    private final JLabel recordsLabel;
    private final JComboBox recordsDropdown;
    private final JButton recordsButton;

    private final JButton createButton;
    private final JButton updateButton;
    private final JButton deleteButton;
    private final JButton logoutButton;

    private String currentRecord;

    private final JTable table;

    public AdminUI(AppFrame app, UserService u, StaffService s, EventHallService h, EquipmentService q, ReservationService r) {
        this.app = app;
        this.userService = u;
        this.staffService = s;
        this.eventHallService = h;
        this.equipmentService = q;
        this.reservationService = r;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        mainLabel = app.createLabel("View Records Tables", Color.BLUE, 60f, 3);

        recordsButton = app.createButton("View Records", Color.BLUE, 24f, false);
        recordsButton.setMinimumSize(new Dimension(300, 60));
        recordsButton.setMaximumSize(new Dimension(300, 60));

        recordsLabel = app.createLabel("Record: ", Color.BLACK, 16f, 2);

        recordsDropdown = new JComboBox<>(new String[]{"Reservations", "Users", "Staff", "Event Halls", "Equipment", "Equipment Allocations"});
        recordsDropdown.setFont(app.getRegularFont().deriveFont(14f));
        recordsDropdown.setBackground(Color.WHITE);
        recordsDropdown.setMaximumSize(new Dimension(200, 60));
        ((JLabel) recordsDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        recordsDropdown.addActionListener(e -> refreshTable());

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.add(recordsLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(8, 0)));
        filterPanel.add(recordsDropdown);
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        model = new DefaultTableModel();

        table = new JTable(model);
        table.setRowHeight(40);
        table.getTableHeader().setFont(app.getBoldFont().deriveFont(16f));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setFont(app.getRegularFont().deriveFont(16f));

        DefaultTableCellRenderer paddingRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Set padding: top, left, bottom, right
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                return c;
            }
        };

        table.setDefaultRenderer(Object.class, paddingRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        createButton = app.createButton("Create", Color.BLUE, 24f, false);
        updateButton = app.createButton("Update", Color.BLUE, 24f, false);
        deleteButton = app.createButton("Delete", Color.BLUE, 24f, false);
        logoutButton = app.createButton("Logout (Return to Login)", Color.decode("#F94449"), 24f, false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        buttonPanel.add(createButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(16, 0)));
        buttonPanel.add(updateButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(16, 0)));
        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(740, 0)));
        buttonPanel.add(logoutButton);

        add(Box.createVerticalStrut(40));
        add(mainLabel);
        add(Box.createVerticalStrut(80));
        add(filterPanel);
        add(Box.createVerticalStrut(40));
        add(scrollPane);
        add(Box.createVerticalStrut(40));
        add(buttonPanel);
        add(Box.createVerticalStrut(40));

        refreshTable();
    }

    private void refreshTable() {
        currentRecord = (String) recordsDropdown.getSelectedItem();

        switch (currentRecord) {
            case "Users" -> {
                showUsersTable();
                createButton.addActionListener(e -> {
                    new UsersForm(app, userService, null).setVisible(true);
                    refreshTable();
                });

                updateButton.addActionListener(e -> {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(this, "Select a user to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int userId = (int) table.getValueAt(row, 0);
                    User us = userService.getUserById(userId);
                    new UsersForm(app, userService, us).setVisible(true);
                    refreshTable();
                });
            }

            case "Staff" -> {
                showStaffTable();
                createButton.addActionListener(e -> {
                    new StaffForm(app, staffService, null).setVisible(true);
                    refreshTable();
                });

                updateButton.addActionListener(e -> {
                    int row = table.getSelectedRow();
                    if (row == -1) {
                        JOptionPane.showMessageDialog(this, "Select a staff to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int staffId = (int) table.getValueAt(row, 0);
                    Staff st = staffService.getStaffById(staffId);
                    new StaffForm(app, staffService, st).setVisible(true);
                    refreshTable();
                });
            }
        }
        if (currentRecord.equals("Users")) showUsersTable();
        if (currentRecord.equals("Event Halls")) showEventHallsTable();
        if (currentRecord.equals("Equipment")) showEquipmentTable();
        if (currentRecord.equals("Reservations")) showReservationsTable();
    }

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
            model.addRow(new Object[] {
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
            model.addRow(new Object[] {
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
            model.addRow(new Object[] {
                    hall.getHallId(),
                    hall.getHallName(),
                    hall.getCapacity(),
                    hall.getLocation(),
                    hall.getStatus()
            });
        }
    }

    private void showEquipmentTable() {
        model.setRowCount(0);
        model.setColumnCount(0);

        model.addColumn("Equipment ID");
        model.addColumn("Name");
        model.addColumn("Total Quantity");
        model.addColumn("Status");

        List<Equipment> equipments = equipmentService.getAllEquipments();

        for (Equipment equipment : equipments) {
            model.addRow(new Object[] {
                    equipment.getEquipmentId(),
                    equipment.getEquipmentName(),
                    equipment.getQuantityTotal(),
                    equipment.getEquipment()
            });
        }
    }

    private void showReservationsTable() {
        model.setRowCount(0);
        model.setColumnCount(0);

        model.addColumn("Reservation ID");
        model.addColumn("Event Hall");
        model.addColumn("Customer");
        model.addColumn("Email");
        model.addColumn("Staff-In-Charge");
        model.addColumn("Date Reserved");
        model.addColumn("Event Type");
        model.addColumn("Guest Count");
        model.addColumn("Created On");
        model.addColumn("Status");

        List<Reservation> reservations = reservationService.getAllReservations();

        for (Reservation reservation : reservations) {
            model.addRow(new Object[] {
                    reservation.getReservationId(),
                    eventHallService.getHallById(reservation.getHallId()).getHallName(),
                    userService.getUserById(reservation.getUserId()).getName(),
                    userService.getUserById(reservation.getUserId()).getEmail(),
                    staffService.getStaffById(reservation.getStaffId()).getName(),
                    reservation.getEventDate(),
                    reservation.getEventType(),
                    reservation.getGuestCount(),
                    reservation.getCreatedOn(),
                    reservation.getStatus()
            });
        }
    }

}
