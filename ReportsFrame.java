package com.inventory.JavaProject;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import com.inventory.JavaProject.LineChartExample;
import static com.inventory.JavaProject.LineChartExample.createDataset;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartPanel;

public class ReportsFrame extends JFrame {

    public ReportsFrame() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Create a panel for the sidebar
        JPanel sidebarPanel = createSidebarPanel();

        // Create the main content panel
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Upper-left panel with total profit, revenue, cost, etc.
        JPanel upperLeftPanel = new RoundedPanel(15);
        upperLeftPanel.setLayout(new GridLayout(2, 3, 0, 10));
        upperLeftPanel.setBackground(Color.WHITE);

        // Add labels and values to upperLeftPanel
        // Use placeholders for the values, replace with actual data
        JLabel totalProfitLabel = new JLabel("Total Profit:");
        JLabel totalProfitValue = new JLabel("$100,000");

        JLabel revenueLabel = new JLabel("Revenue:");
        JLabel revenueValue = new JLabel("$200,000");

        JLabel costLabel = new JLabel("Cost:");
        JLabel costValue = new JLabel("$100,000");

        JLabel monthlyProfitLabel = new JLabel("Monthly Profit:");
        JLabel monthlyProfitValue = new JLabel("$20,000");

        JLabel yearlyProfitLabel = new JLabel("Yearly Profit:");
        JLabel yearlyProfitValue = new JLabel("$150,000");

        // Add labels and values to the upperLeftPanel
        upperLeftPanel.add(totalProfitLabel);
        upperLeftPanel.add(totalProfitValue);
        upperLeftPanel.add(revenueLabel);
        upperLeftPanel.add(revenueValue);
        upperLeftPanel.add(costLabel);
        upperLeftPanel.add(costValue);
        upperLeftPanel.add(monthlyProfitLabel);
        upperLeftPanel.add(monthlyProfitValue);
        upperLeftPanel.add(yearlyProfitLabel);
        upperLeftPanel.add(yearlyProfitValue);

        // Upper-right panel with best selling categories
        JPanel upperRightPanel = new RoundedPanel(15);
        upperRightPanel.setLayout(new BorderLayout());
        upperRightPanel.setBackground(Color.WHITE);

        // Use a JTable for best selling categories
        String[] columnNames = {"Category Name", "Turnover", "Net Profit"};
        Object[][] data = {{"Category1", "$10,000", "$5,000"}, {"Category2", "$15,000", "$7,000"}};
        JTable categoriesTable = new JTable(data, columnNames);
        JScrollPane categoriesScrollPane = new JScrollPane(categoriesTable);
        upperRightPanel.add(categoriesScrollPane, BorderLayout.CENTER);
        
        
        // Create the bottom panel with profit & revenue over time chart
        JPanel bottomPanel = new RoundedPanel(15);
        bottomPanel.setBackground(Color.WHITE);

        // Create the dataset for the line chart
        XYDataset dataset = LineChartExample.createDataset("C:\\Users\\USER\\Documents\\NetBeansProjects\\Inventory\\src\\main\\java\\com\\inventory\\JavaProject\\report.csv", 0, 0);

        // Create the line chart using the dataset
        JFreeChart lineChart = LineChartExample.createChart(dataset);

        // Create a ChartPanel to display the chart
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(800, 400));

        // Add the ChartPanel to the bottomPanel
        bottomPanel.add(chartPanel);

        // Create the panel for best selling product
        JPanel bestSellingProductPanel = new RoundedPanel(15);
        bestSellingProductPanel.setBackground(Color.WHITE);

        // Placeholder for best selling product information
        JLabel bestSellingProductLabel = new JLabel("Best Selling Product");
        bestSellingProductPanel.add(bestSellingProductLabel);

        // Add the upper panels to the main content panel
        mainContentPanel.add(upperLeftPanel, BorderLayout.WEST);
        mainContentPanel.add(upperRightPanel, BorderLayout.EAST);

        // Add the bottom panel to the main content panel
        mainContentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the best selling product panel to the main content panel
        mainContentPanel.add(bestSellingProductPanel, BorderLayout.CENTER);

        // Add the sidebar panel and main content panel to the frame
        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        // Set the frame visible
        setVisible(true);
    }



    // ---------- SideBarPanel Here ---------- //
    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(240, 241, 243)); // #F0F1F3
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10)); // Add padding

        JLabel marketLabel = new JLabel("UNO Market");
        marketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        marketLabel.setFont(new Font("Poppins", Font.BOLD, 16)); // Use Poppins font
        sidebarPanel.add(marketLabel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add vertical spacing

        String[] pageNames = {"Dashboard", "Inventory", "Reports", "Suppliers", "Orders", "Logout"};
        for (String pageName : pageNames) {
            JButton pageButton = new JButton(pageName);
            pageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            pageButton.setFont(new Font("Poppins", Font.PLAIN, 14)); // Use Poppins font
            pageButton.setBackground(new Color(240, 241, 243)); // #F0F1F3
            pageButton.setBorderPainted(false); // No border
            pageButton.setFocusPainted(false); // No focus border
            pageButton.setContentAreaFilled(false); // No background
            pageButton.setForeground(new Color(93, 102, 121)); // Font color: #5D6679

            pageButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    handlePageNavigation(pageName);
                }
            });

            // Highlight the current page button
            if (getCurrentPage().equalsIgnoreCase(pageName)) {
                pageButton.setForeground(new Color(21, 112, 239)); // Font color: #1570EF
            }

            sidebarPanel.add(pageButton);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        }

        return sidebarPanel;
    }

    private String getCurrentPage() {
        // Get the simple name of the class of the active frame
        String className = getClass().getSimpleName();
        System.out.println(className);
        // Map the class name to the corresponding page name
        switch (className) {
            case "DashboardFrame":
                return "Dashboard";
            case "InventoryFrame":
                return "Inventory";
            case "ReportsFrame":
                return "Reports";
            case "SuppliersFrame2":
                return "Suppliers";
            case "OrdersFrame":
                return "Orders";
            case "LoginFrame":
                return "Logout";
            default:
                return "UNKOWN"; // Handle unknown class names or return a default value
        }
    }


    private void handlePageNavigation(String pageName) {
        // Dispose of the current frame
        dispose();

        // Implement logic to switch between pages based on the button clicked
        switch (pageName) {
            case "Dashboard":
                // Open the DashboardFrame
                //new DashboardFrame().setVisible(true);
                break;
            case "Inventory":
                // Open the InventoryFrame
                new InventoryFrame().setVisible(true);
                break;
            case "Reports":
                // Open the ReportsFrame
                //new ReportsFrame().setVisible(true);
                break;
            case "Suppliers":
                // Open the SuppliersFrame
                new SuppliersFrame2().setVisible(true);
                break;
            case "Orders":
                // Open the OrdersFrame
                new OrdersFrame().setVisible(true);
                break;
            case "Logout":
                // Open the logutFrame
                new LoginFrame().setVisible(true);
                break;
            default:
                // Handle unknown page names
                System.out.println("Unknown page: " + pageName);
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReportsFrame().setVisible(true));
    }
}

class RoundedBorder implements Border {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}

class RoundedPanel extends JPanel {
    private int cornerRadius;

    RoundedPanel(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the rounded panel with borders.
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
        graphics.setColor(getForeground());
        graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
    }
}
