
package com.inventory.JavaProject;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LineChartExample extends JFrame {

    public LineChartExample() {
        super("Profit & Revenue Over Time Chart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Read data from the CSV file
        XYDataset dataset = LineChartExample.createDataset("C:\\Users\\USER\\Documents\\NetBeansProjects\\Inventory\\src\\main\\java\\com\\inventory\\JavaProject\\report.csv", 0, 1);
        // Create the chart
        JFreeChart chart = createChart(dataset);

        // Create a panel to display the chart
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Add the chart panel to the frame
        add(chartPanel);
    }

     public static XYDataset createDataset(String filePath, int xColumn, int yColumn) {
    XYSeries seriesProfit = new XYSeries("Total Profit");
    XYSeries seriesRevenue = new XYSeries("Net Revenue");

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        // Skip the first line (headers)
        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length == 4) {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data[0]);
                double totalProfit = Double.parseDouble(data[1]);
                double netRevenue = Double.parseDouble(data[3]);

                seriesProfit.add(date.getTime(), totalProfit);
                seriesRevenue.add(date.getTime(), netRevenue);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(seriesProfit);
    dataset.addSeries(seriesRevenue);

    return dataset;
}

    public static JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Profit & Revenue Over Time",
                "Date",
                "Amount",
                dataset,
                false,
                true,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        axis.setTickUnit(new DateTickUnit(DateTickUnitType.MONTH, 1));

        return chart;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LineChartExample().setVisible(true));
    }
}
