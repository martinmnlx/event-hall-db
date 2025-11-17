package com.infom.eventhall.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.infom.eventhall.model.EquipmentAllocation;
import com.infom.eventhall.model.EventHall;

import com.infom.eventhall.model.Reservation;
import com.infom.eventhall.service.ReservationService;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class ReserveUI extends JPanel {

    private final AppFrame app;

    private final ReservationService reservationService;

    private int hallID;
    private EventHall hall;

    private final JLabel titleLabel;
    private final JLabel hallLabel;
    private final JLabel locationLabel;
    private final JLabel typeLabel;
    private final JLabel guestLabel;
    private final JLabel dateLabel;
    private final JLabel equipmentLabel;
    private final JTextField hallField;
    private final JTextField locationField;
    private final JComboBox<String> typeDropdown;
    private final JSpinner guestSpinner;
    private final JButton confirmButton;
    private final JButton cancelButton;

    private ArrayList<JCheckBox> boxes;
    private final JCheckBox fogMachine;
    private final JCheckBox laserLights;
    private final JCheckBox karaoke;
    private final JCheckBox photoBooth;

    private final JDatePanelImpl datePanel;
    private final JDatePickerImpl datePicker;

    public ReserveUI(AppFrame app, ReservationService reservationService) {
        this.app = app;
        this.reservationService = reservationService;

        UtilDateModel model = new UtilDateModel();
        model.setSelected(true); // sets today as default

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new JFormattedTextField.AbstractFormatter() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            @Override
            public Object stringToValue(String text) throws ParseException { return df.parse(text); }
            @Override
            public String valueToString(Object value) {
                if (value != null) return df.format(((Calendar)value).getTime());
                return "";
            }
        });

        JFormattedTextField tf = datePicker.getJFormattedTextField();
        tf.setPreferredSize(new Dimension(200, 40));
        tf.setFont(app.getRegularFont().deriveFont(16f));
        tf.setMargin(new Insets(8, 8, 8, 8));
        tf.setBackground(Color.WHITE);

        Component btn = null;
        if (datePicker.getComponentCount() > 1) btn = datePicker.getComponent(1);
        if (btn != null) {
            btn.setPreferredSize(new Dimension(30, 40));
        }

        datePicker.setMaximumSize(new Dimension(300, 40));
        datePicker.setAlignmentX(Component.LEFT_ALIGNMENT);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(350, 600));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = app.createLabel("Book a Reservation", Color.BLUE, 60f, 3);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hallLabel = app.createLabel("Event Hall", Color.BLACK, 20f, 2);
        hallLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        locationLabel = app.createLabel("Location", Color.BLACK, 20f, 2);
        locationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        typeLabel = app.createLabel("Select Event Type", Color.BLACK, 20f, 2);
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        guestLabel = app.createLabel("Enter Guest Count", Color.BLACK, 20f, 2);
        guestLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        dateLabel = app.createLabel("Select Event Date", Color.BLACK, 20f, 2);
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        equipmentLabel = app.createLabel("Select Special Equipment", Color.BLACK, 20f, 2);
        equipmentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        hallField = new JTextField();
        hallField.setMaximumSize(new Dimension(300, 60));
        hallField.setMargin(new Insets(8, 8, 8, 8));
        hallField.setFont(app.getRegularFont().deriveFont(16f));
        hallField.setEnabled(false);
        hallField.setDisabledTextColor(Color.GRAY);
        hallField.setAlignmentX(Component.LEFT_ALIGNMENT);

        locationField = new JTextField();
        locationField.setMaximumSize(new Dimension(300, 60));
        locationField.setMargin(new Insets(8, 8, 8, 8));
        locationField.setFont(app.getRegularFont().deriveFont(16f));
        locationField.setEnabled(false);
        locationField.setDisabledTextColor(Color.GRAY);
        locationField.setAlignmentX(Component.LEFT_ALIGNMENT);

        typeDropdown = new JComboBox<>(new String[]{"Birthday", "Wedding", "Meeting", "Conference", "Other"});
        typeDropdown.setFont(app.getRegularFont().deriveFont(16f));
        typeDropdown.setMaximumSize(new Dimension(300, 60));
        typeDropdown.setBackground(Color.WHITE);
        ((JLabel) typeDropdown.getRenderer()).setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        typeDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);

        guestSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 10));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) guestSpinner.getEditor();
        editor.getTextField().setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        editor.getTextField().setFont(app.getRegularFont().deriveFont(16f));
        editor.getTextField().setHorizontalAlignment(JTextField.LEFT);
        guestSpinner.setMaximumSize(new Dimension(300, 60));
        guestSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        infoPanel.add(hallLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(hallField);
        infoPanel.add(Box.createVerticalStrut(16));
        infoPanel.add(locationLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(locationField);
        infoPanel.add(Box.createVerticalStrut(16));
        infoPanel.add(typeLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(typeDropdown);
        infoPanel.add(Box.createVerticalStrut(16));
        infoPanel.add(guestLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(guestSpinner);
        infoPanel.add(Box.createVerticalStrut(16));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(datePicker);
        infoPanel.add(Box.createVerticalStrut(16));
        infoPanel.add(equipmentLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel equipmentPanel = new JPanel();
        equipmentPanel.setLayout(new BoxLayout(equipmentPanel, BoxLayout.X_AXIS));
        equipmentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add checkboxes (or other components) inside a vertical BoxLayout panel
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        checkBoxPanel.add(fogMachine = createCheckBox("Fog Machine"));
        checkBoxPanel.add(laserLights = createCheckBox("Laser Lights"));
        checkBoxPanel.add(karaoke = createCheckBox("Karaoke System"));
        checkBoxPanel.add(photoBooth = createCheckBox("Photo Booth"));

        boxes = new ArrayList<>();
        boxes.add(fogMachine);
        boxes.add(laserLights);
        boxes.add(karaoke);
        boxes.add(photoBooth);

        equipmentPanel.add(checkBoxPanel);

        confirmButton = app.createButton("Confirm Booking", Color.BLUE, 20f, true);
        confirmButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        cancelButton = app.createButton("Cancel", Color.decode("#F94449"), 20f, true);
        cancelButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(confirmButton);
        buttonPanel.add(Box.createHorizontalStrut(28));
        buttonPanel.add(cancelButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(infoPanel);
        card.add(equipmentPanel);
        card.add(Box.createVerticalStrut(20));
        card.add(buttonPanel);
        card.add(Box.createVerticalGlue());

        add(Box.createVerticalGlue());
        add(titleLabel);
        add(Box.createVerticalStrut(40));
        add(card);
        add(Box.createVerticalGlue());

        confirmButton.addActionListener(e -> handleReservation());
        cancelButton.addActionListener(e -> app.showScreen("halls"));
    }

    public void refresh() {
        hallID = app.getHallsUI().getChosenHallID();
        hall = app.getDb().getEventHallDAO().getHallById(hallID);

        hallField.setText(hall.getHallName());
        locationField.setText(hall.getLocation());
        SpinnerNumberModel model = (SpinnerNumberModel) guestSpinner.getModel();
        model.setMaximum(hall.getCapacity());
        model.setValue(hall.getCapacity());

        System.out.println("User chose hall: " + hall.getHallName());
    }

    public void handleReservation() {
        Date selectedDate = (Date) datePicker.getModel().getValue(); // returns java.util.Date
        LocalDate date = selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        String type = (String) typeDropdown.getSelectedItem();
        int guests = (int) guestSpinner.getValue();

        Reservation r = new Reservation();
        r.setHallId(hall.getHallId());
        r.setUserId(102);
        r.setStaffId(101);
        r.setCreatedOn(LocalDateTime.now());
        r.setEventDate(date);
        r.setEventType(type);
        r.setGuessCount(guests);
        r.setStatus(Reservation.ReservationStatus.Pending);

        app.getDb().getReservationDAO().createReservation(r);
    }

    private JCheckBox createCheckBox(String text) {
        JCheckBox cb = new JCheckBox(text);

        cb.setIconTextGap(12);
        cb.setFont(app.getRegularFont().deriveFont(16f));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        cb.setMargin(new Insets(4, 8, 4, 8));

        return cb;
    }
}
