package com.infom.eventhall.ui;

import com.infom.eventhall.service.*;
import com.infom.eventhall.ui.forms.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset; // NEW IMPORT FOR PIE CHART

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportsUI extends JPanel {

    private final AppFrame app;

    private final ReportService reportService;

    private final DefaultTableModel model;
    private final JTable table;
    private final JScrollPane scrollPane;
    private final JPanel contentPanel;

    private final JLabel mainLabel;
    private final JComboBox<String> recordsDropdown;
    private final JButton generateButton;
    private final JButton logoutButton;

    private final JPanel filterContainer; // The panel that swaps views
    private final CardLayout filterLayout; // The layout manager

    private JComboBox<String> monthDropdown;
    private JSpinner yearSpinner;

    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;

    public ReportsUI(AppFrame app, ReportService r) {
        this.app = app;
        this.reportService = r;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        mainLabel = app.createLabel("Generate Reports", Color.BLUE, 60f, 3);
        mainLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- 1. TOP SELECTION PANEL ---
        JLabel recordsLabel = app.createLabel("Report Type: ", Color.BLACK, 20f, 2);
        recordsDropdown = new JComboBox<>(new String[]{
                "Monthly Hall Reservations",
                "Hall Utilization (Booked vs Available)",
                "Equipment Popularity",
                "Event Type Popularity"
        });
        recordsDropdown.setMaximumSize(new Dimension(220, 60));
        styleDropdown(recordsDropdown);

        JPanel topSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topSelectionPanel.add(recordsLabel);
        topSelectionPanel.add(recordsDropdown);
        topSelectionPanel.add(Box.createHorizontalStrut(24));
        topSelectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- 2. CREATE FILTER CARDS ---

        // Card 1 Components: Month/Year
        JPanel monthYearPanel = createMonthYearPanel();

        // Card 2 Components: Date Range
        JPanel dateRangePanel = createDateRangePanel();

        // 3. Setup CardLayout Container
        filterLayout = new CardLayout();
        filterContainer = new JPanel(filterLayout);
        filterContainer.add(monthYearPanel, "MONTH_YEAR");
        filterContainer.add(dateRangePanel, "DATE_RANGE");
        filterContainer.add(new JPanel(), "NONE");
        filterContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 4. Listener to Swap Cards
        recordsDropdown.addActionListener(e -> {
            String selected = (String) recordsDropdown.getSelectedItem();
            if ("Monthly Hall Reservations".equals(selected)) {
                filterLayout.show(filterContainer, "MONTH_YEAR");
            } else if ("Hall Utilization (Booked vs Available)".equals(selected) || "Equipment Popularity".equals(selected)) {
                filterLayout.show(filterContainer, "DATE_RANGE");
            } else {
                filterLayout.show(filterContainer, "NONE");
            }
            // Execute refresh immediately after swap
            refresh();
        });

        // Default to showing the first card
        filterLayout.show(filterContainer, "MONTH_YEAR");

        topSelectionPanel.add(filterContainer);

        // --- Table & Chart Container Setup (Same as before) ---
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
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, paddingRenderer);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));

        // --- Bottom Buttons (Moved Generate to top selection area) ---
        generateButton = app.createButton("Generate Report", Color.BLUE, 20f, false);
        generateButton.addActionListener(e -> refresh());

        logoutButton = app.createButton("Return to Dashboard", Color.BLUE, 20f, false);
        logoutButton.addActionListener(e -> app.showScreen("admin"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.add(generateButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(980, 0)));
        buttonPanel.add(logoutButton);


        // --- Add Components to Main Panel ---
        add(Box.createVerticalStrut(40));
        add(mainLabel);
        add(Box.createVerticalStrut(60));
        add(topSelectionPanel);
        add(Box.createVerticalStrut(40));
        add(contentPanel);
        add(Box.createVerticalStrut(40));
        add(buttonPanel);
        add(Box.createVerticalStrut(40));

        refresh();
    }

    // --- Helper: Create Month/Year Card ---
    private JPanel createMonthYearPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        monthDropdown = new JComboBox<>(months);
        styleDropdown(monthDropdown);
        monthDropdown.setSelectedIndex(java.time.LocalDate.now().getMonthValue() - 1);

        int currentYear = Year.now().getValue();
        yearSpinner = new JSpinner(new SpinnerNumberModel(currentYear, 2000, 2100, 1));
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearSpinner, "#");
        yearSpinner.setEditor(editor);
        styleSpinner(yearSpinner);

        p.add(app.createLabel("Month: ", Color.BLACK, 20f, 2));
        p.add(monthDropdown);
        p.add(Box.createHorizontalStrut(16));
        p.add(app.createLabel("Year: ", Color.BLACK, 20f, 2));
        p.add(yearSpinner);
        return p;
    }

    // --- Helper: Create Date Range Card ---
    private JPanel createDateRangePanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        // Start Date (Default to 1 month ago)
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "dd-MM-yyyy"));
        startDateSpinner.setValue(java.sql.Date.valueOf(LocalDate.now().minusMonths(1)));
        styleSpinner(startDateSpinner);

        // End Date (Default to today)
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "dd-MM-yyyy"));
        endDateSpinner.setValue(new Date());
        styleSpinner(endDateSpinner);

        p.add(app.createLabel("Start Date: ", Color.BLACK, 20f, 2));
        p.add(startDateSpinner);
        p.add(Box.createHorizontalStrut(20));
        p.add(app.createLabel("End Date: ", Color.BLACK, 20f, 2));
        p.add(endDateSpinner);

        return p;
    }

    private void styleDropdown(JComboBox<String> box) {
        box.setFont(app.getRegularFont().deriveFont(16f));
        box.setBackground(Color.WHITE);
        ((JLabel) box.getRenderer()).setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    }

    private void styleSpinner(JSpinner sp) {
        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)editor).getTextField().setFont(app.getRegularFont().deriveFont(16f));
            ((JSpinner.DefaultEditor)editor).getTextField().setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            ((JSpinner.DefaultEditor)editor).getTextField().setHorizontalAlignment(JTextField.LEFT);
        }
    }

    public void refresh() {
        // Clear existing data (only needed if table is shown)
        model.setRowCount(0);
        model.setColumnCount(0);

        String selectedReport = (String) recordsDropdown.getSelectedItem();

        // --- REPORT 1: MONTHLY HALL RESERVATIONS (Bar Chart) ---
        if ("Monthly Hall Reservations".equals(selectedReport)) {
            int month = monthDropdown.getSelectedIndex() + 1;
            int year = (Integer) yearSpinner.getValue();

            List<Map<String, Object>> data = reportService.getMonthlyReservationPerHall(month, year);

            if (data == null || data.isEmpty()) { showNoData(); return; }

            // Use Bar Chart method
            updateBarChart(data, "Reservations per Hall (" + monthDropdown.getSelectedItem() + " " + year + ") Report", "Hall Name", "Bookings", "hallName", "reservationCount");
        }

        // --- REPORT 2: HALL UTILIZATION (Bar Chart) ---
        else if ("Hall Utilization (Booked vs Available)".equals(selectedReport)) { // **FIXED: Changed if to else if**
            Date startUtil = (Date) startDateSpinner.getValue();
            Date endUtil = (Date) endDateSpinner.getValue();

            LocalDate start = startUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = endUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // NOTE: Assuming you have a reportService.getHallUtilizationReport(start, end) method
            List<Map<String, Object>> data = reportService.getHallUtilizationReport(start, end);

            if (data == null || data.isEmpty()) { showNoData(); return; }

            // Use Bar Chart method
            updateBarChart(data, "Hall Utilization Report (" + start + " to " + end + ")", "Hall Name", "Days Booked", "hallName", "daysBooked");
        }

        // --- REPORT 3: EQUIPMENT POPULARITY (Pie Chart) ---
        else if ("Equipment Popularity".equals(selectedReport)) {
            Date startUtil = (Date) startDateSpinner.getValue();
            Date endUtil = (Date) endDateSpinner.getValue();

            // Convert java.util.Date to java.time.LocalDate for your service layer
            LocalDate start = startUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = endUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            List<Map<String, Object>> data = reportService.getEquipmentUtilizationReport(start, end);

            if (data == null || data.isEmpty()) { showNoData(); return; }

            // Use NEW Pie Chart method
            updatePieChart(data, "Equipment Popularity Report (" + start + " to " + end + ")", "equipmentName", "quantityRented");
        }

        // --- REPORT 4: EVENT TYPE POPULARITY (Pie Chart) ---
        else if ("Event Type Popularity".equals(selectedReport)) {
            List<Map<String, Object>> data = reportService.getEventTypeReport();

            if (data == null || data.isEmpty()) { showNoData(); return; }

            // Use NEW Pie Chart method
            updatePieChart(data, "Event Type Popularity Report (All Time)", "eventType", "count");
        }

        // --- FALLBACK (UNHANDLED REPORTS) ---
        else {
            showTable();
            model.setColumnIdentifiers(new String[]{"Info"});
            // Added a note for the new unhandled report
            model.addRow(new Object[]{"Report: " + selectedReport + " not yet implemented with visualization."});
        }
    }

    // --- HELPER METHOD TO SWAP TO TABLE (Fallback) ---
    private void showTable() {
        contentPanel.removeAll();
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // --- GENERIC BAR CHART HELPER METHOD ---
    private void updateBarChart(List<Map<String, Object>> data, String title, String xLabel, String yLabel, String mapKeyName, String mapValueKey) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map<String, Object> row : data) {
            String category = (String) row.get(mapKeyName);
            Number value = (Number) row.get(mapValueKey);

            if (value != null) dataset.addValue(value, yLabel, category);
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                title, xLabel, yLabel, dataset,
                PlotOrientation.VERTICAL, true, true, false
        );

        barChart.setBackgroundPaint(Color.WHITE);
        barChart.getPlot().setBackgroundPaint(new Color(240, 240, 240));

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        contentPanel.removeAll();
        contentPanel.add(chartPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // --- NEW PIE CHART HELPER METHOD ---
    private void updatePieChart(List<Map<String, Object>> data, String title, String mapKeyName, String mapValueKey) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map<String, Object> row : data) {
            String category = (String) row.get(mapKeyName);
            Number value = (Number) row.get(mapValueKey);

            if (value != null) {
                // Pie Chart uses setValue(Key, Value)
                dataset.setValue(category, value);
            }
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                title,      // Chart title
                dataset,    // Data
                true,       // Include legend
                true,       // Tooltips
                false       // URLs
        );

        pieChart.setBackgroundPaint(Color.WHITE);

        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        contentPanel.removeAll();
        contentPanel.add(chartPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // --- HELPER FOR NO DATA ---
    private void showNoData() {
        contentPanel.removeAll();
        JLabel label = app.createLabel("No data found for the selected period.", Color.GRAY, 18f, 2);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(label, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}