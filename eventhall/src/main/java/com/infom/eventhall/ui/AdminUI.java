package com.infom.eventhall.ui;

import com.infom.eventhall.model.Reservation;
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
    private final ReservationService reservationService;

    private final JTable table;

    public AdminUI(AppFrame app, UserService u, StaffService s, EventHallService h, ReservationService r) {
        this.app = app;
        this.userService = u;
        this.staffService = s;
        this.eventHallService = h;
        this.reservationService = r;

        setLayout(new BorderLayout());

        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(300, 0));

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Reservation ID");
        model.addColumn("Hall ID");
        model.addColumn("User ID");
        model.addColumn("Staff ID");
        model.addColumn("Date");
        model.addColumn("Event Type");
        model.addColumn("Guest Count");
        model.addColumn("Status");

        List<Reservation> reservations = reservationService.getAllReservations();

        for (Reservation reservation : reservations) {
            model.addRow(new Object[] {
                    reservation.getReservationId(),
                    reservation.getHallId(),
                    reservation.getUserId(),
                    reservation.getStaffId(),
                    reservation.getEventDate(),
                    reservation.getEventType(),
                    reservation.getGuestCount(),
                    reservation.getStatus()
            });
        }

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
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(sidePanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);

    }

}
