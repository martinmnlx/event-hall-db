package com.infom.eventhall.ui;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date; // Import added
import java.util.Calendar; // Import added
import java.util.List;
import java.util.Properties;

import com.infom.eventhall.model.EventHall;
import com.infom.eventhall.service.EventHallService;
import lombok.Getter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class HallsUI extends JPanel {

    private final AppFrame app;
    private final EventHallService eventHallService;

    private final JPanel hallsPanel;
    private final JComboBox<String> cityDropdown;
    private final JTextField nameSearch;
    private final JSpinner minCapacity;
    private final JComboBox<String> statusDropdown;
    private final JLabel numResults;

    private List<EventHall> halls;
    private List<String> cities;

    private final JDatePanelImpl datePanel;

    @Getter
    private final JDatePickerImpl datePicker;

    @Getter
    private int chosenHallID = 0;

    public HallsUI(AppFrame app, EventHallService h) {
        this.app = app;
        this.eventHallService = h;

        halls = eventHallService.getAllEventHalls();
        cities = app.getDb().getEventHallDAO().getDistinctLocations();

        cities.add(0,"All");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = app.createLabel("Browse Event Halls", Color.BLUE, 60f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));

        nameSearch = new JTextField(12);
        nameSearch.setMargin(new Insets(4, 4, 4, 4));
        nameSearch.setFont(app.getRegularFont().deriveFont(14f));

        cityDropdown = new JComboBox<>(cities.toArray(new String[0]));
        cityDropdown.setFont(app.getRegularFont().deriveFont(14f));
        cityDropdown.setBackground(Color.WHITE);
        ((JLabel) cityDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        // --- Capacity Spinner ---
        minCapacity = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 10));
        JSpinner.DefaultEditor capEditor = (JSpinner.DefaultEditor) minCapacity.getEditor();
        capEditor.getTextField().setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        capEditor.getTextField().setFont(app.getRegularFont().deriveFont(14f));

        // --- Date Picker ---
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new JFormattedTextField.AbstractFormatter() {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            @Override
            public Object stringToValue(String text) throws ParseException { return df.parse(text); }
            @Override
            public String valueToString(Object value) {
                if (value != null) return df.format(((Calendar)value).getTime());
                return "";
            }
        });

        JFormattedTextField tf = datePicker.getJFormattedTextField();
        tf.setPreferredSize(new Dimension(120, 32));
        tf.setFont(app.getRegularFont().deriveFont(14f));
        tf.setMargin(new Insets(4, 4, 4, 4));
        tf.setBackground(Color.WHITE);

        Component btn = null;
        if (datePicker.getComponentCount() > 1) btn = datePicker.getComponent(1);
        if (btn != null) {
            btn.setPreferredSize(new Dimension(30, 32));
        }

        statusDropdown = new JComboBox<>(new String[]{"Available", "Booked", "Maintenance", "All"});
        statusDropdown.setFont(app.getRegularFont().deriveFont(14f));
        statusDropdown.setBackground(Color.WHITE);
        ((JLabel) statusDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        // --- Add Components to Filter Panel ---
        filterPanel.add(app.createLabel("Name: ", Color.BLACK, 16f, 2));
        filterPanel.add(nameSearch);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(app.createLabel("Date: ", Color.BLACK, 16f, 2));
        filterPanel.add(datePicker);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(app.createLabel("Min. Capacity: ", Color.BLACK, 16f, 2));
        filterPanel.add(minCapacity);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(app.createLabel("City: ", Color.BLACK, 16f, 2));
        filterPanel.add(cityDropdown);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(app.createLabel("Status: ", Color.BLACK, 16f, 2));
        filterPanel.add(statusDropdown);
        filterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        filterPanel.add(numResults = app.createLabel("0 Results", Color.BLUE, 16f, 3));

        // --- Halls panel inside scroll pane ---
        hallsPanel = new JPanel();
        hallsPanel.setLayout(new BoxLayout(hallsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(hallsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JButton dashboardButton = app.createButton("Return to Dashboard", Color.BLUE, 16f, false);
        dashboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(40));
        add(titleLabel);
        add(Box.createVerticalStrut(40));
        add(filterPanel);
        add(Box.createVerticalStrut(40));
        add(scrollPane);
        add(Box.createVerticalStrut(40));
        add(dashboardButton);
        add(Box.createVerticalStrut(40));

        // --- Listeners ---
        cityDropdown.addActionListener(e -> refresh());

        nameSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refresh(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refresh(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refresh(); }
        });

        minCapacity.addChangeListener(e -> refresh());
        statusDropdown.addActionListener(e -> refresh());

        dashboardButton.addActionListener(e -> app.showScreen("dashboard"));
        datePicker.addActionListener(e -> {
            app.getReserveUI().setBookingDate((Date) datePicker.getModel().getValue());
            refresh();
        });

        refresh();
    }

    public void refresh() {
        hallsPanel.removeAll();

        int hallCount = 0;

        String selectedCity = (String) cityDropdown.getSelectedItem();
        String searchText = nameSearch.getText().toLowerCase();
        int minCap = (int) minCapacity.getValue();
        String selectedStatus = (String) statusDropdown.getSelectedItem();
        String computedStatus;
        Date selectedDate = (Date) datePicker.getModel().getValue();
        LocalDate date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        for (EventHall hall : halls) {
            boolean matchesCity = selectedCity.equals("All") || hall.getLocation().equals(selectedCity);
            boolean matchesName = hall.getHallName().toLowerCase().contains(searchText);
            boolean matchesCapacity = hall.getCapacity() >= minCap;
            boolean matchesDate = eventHallService.isHallAvailableOnDate(hall, date);

            if (hall.getStatus().name().equals("Maintenance")) computedStatus = "Maintenance";
            else if (!matchesDate) computedStatus = "Booked";
            else computedStatus = "Available";

            boolean matchesStatus = selectedStatus.equals("All") || selectedStatus.equals(computedStatus);

            if (matchesCity && matchesName && matchesCapacity && matchesStatus) {
                JPanel hallCard = new JPanel();
                hallCard.setLayout(new BoxLayout(hallCard, BoxLayout.X_AXIS));
                hallCard.setAlignmentX(Component.CENTER_ALIGNMENT);
                hallCard.setBorder(
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.GRAY, 1),
                                BorderFactory.createEmptyBorder(20, 20, 20, 20)
                        )
                );
                hallCard.setMinimumSize(new Dimension(760 , 172));
                hallCard.setMaximumSize(new Dimension(760 , 172));

                JPanel main = new JPanel();
                main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

                JPanel side = new JPanel();
                side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

                JPanel status = new JPanel();
                status.setLayout(new BoxLayout(status, BoxLayout.X_AXIS));

                JLabel nameLabel = app.createLabel(hall.getHallName(), Color.BLUE, 32f, 3);
                JLabel capacityLabel = app.createLabel("Capacity: " + hall.getCapacity(), Color.BLACK, 16f, 2);
                JLabel locationLabel = app.createLabel("Location: " + hall.getLocation(), Color.BLACK, 16f, 2);
                JLabel statusLabel = app.createLabel("Status: ", Color.BLACK, 16f, 2);
                JLabel availLabel = app.createLabel(hall.getStatus().name(), Color.decode("#5BB450"), 16f, 3);

                JButton bookButton = app.createButton("Book", Color.BLUE, 20f, false);

                if (computedStatus.equals("Maintenance")) {
                    availLabel.setForeground(Color.decode("#F94449"));
                    bookButton.setEnabled(false);
                    bookButton.setBackground(Color.LIGHT_GRAY);
                    bookButton.setForeground(Color.WHITE);
                    bookButton.setText("Under Maintenance");
                } else if (computedStatus.equals("Booked")) {
                    availLabel.setForeground(Color.decode("#F94449"));
                    availLabel.setText("Booked");
                    bookButton.setEnabled(false);
                    bookButton.setBackground(Color.LIGHT_GRAY);
                    bookButton.setForeground(Color.WHITE);
                    bookButton.setText("Booked");
                }

                bookButton.addActionListener(e -> {
                    chosenHallID = hall.getHallId();
                    app.showScreen("reserve");
                });

                status.add(statusLabel);
                status.add(availLabel);
                status.setAlignmentX(Component.LEFT_ALIGNMENT);

                side.add(Box.createVerticalGlue());
                side.add(bookButton);

                main.add(nameLabel);
                main.add(Box.createVerticalStrut(16));
                main.add(capacityLabel);
                main.add(Box.createVerticalStrut(4));
                main.add(locationLabel);
                main.add(Box.createVerticalStrut(4));
                main.add(status);

                hallCard.add(main);
                hallCard.add(side);

                hallCount ++;

                hallsPanel.add(hallCard);
                hallsPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }

        numResults.setText(hallCount + " Results");

        hallsPanel.revalidate();
        hallsPanel.repaint();
    }

}